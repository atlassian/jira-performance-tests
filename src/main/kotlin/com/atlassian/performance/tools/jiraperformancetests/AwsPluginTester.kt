package com.atlassian.performance.tools.jiraperformancetests

import com.atlassian.performance.tools.aws.Aws
import com.atlassian.performance.tools.aws.Investment
import com.atlassian.performance.tools.awsinfrastructure.InfrastructureFormula
import com.atlassian.performance.tools.awsinfrastructure.jira.JiraFormula
import com.atlassian.performance.tools.awsinfrastructure.jira.StandaloneFormula
import com.atlassian.performance.tools.awsinfrastructure.storage.JiraSoftwareStorage
import com.atlassian.performance.tools.awsinfrastructure.virtualusers.Ec2VirtualUsersFormula
import com.atlassian.performance.tools.infrastructure.Dataset
import com.atlassian.performance.tools.infrastructure.app.AppSource
import com.atlassian.performance.tools.infrastructure.app.Apps
import com.atlassian.performance.tools.infrastructure.virtualusers.GrowingLoadSchedule
import com.atlassian.performance.tools.infrastructure.virtualusers.LoadProfile
import com.atlassian.performance.tools.infrastructure.virtualusers.SshVirtualUsers
import com.atlassian.performance.tools.jiraactions.ActionType
import com.atlassian.performance.tools.jiraactions.scenario.Scenario
import com.atlassian.performance.tools.report.*
import com.atlassian.performance.tools.workspace.api.RootWorkspace
import com.atlassian.performance.tools.workspace.api.TestWorkspace
import com.google.common.util.concurrent.ThreadFactoryBuilder
import java.io.File
import java.nio.file.Path
import java.time.Duration
import java.util.concurrent.Executors

class AwsPluginTester(
    private val aws: Aws,
    private val dataset: Dataset,
    outputDirectory: Path
) {
    private val root: RootWorkspace = RootWorkspace(outputDirectory)
    private val label = "Plugin Test"

    fun run(
        shadowJar: File,
        scenarioClass: Class<out Scenario>,
        actionCriteria: Map<ActionType<*>, Criteria>,
        baselineApp: AppSource,
        experimentApp: AppSource,
        duration: Duration,
        jiraVersion: String
    ): Results {
        val standaloneStabilityTest = RegressionTest(
            dataset,
            shadowJar,
            scenarioClass,
            actionCriteria,
            baselineApp,
            experimentApp,
            duration,
            jiraVersion
        )
        return standaloneStabilityTest.run(
            workspace = root.currentTask.isolateTest(label)
        )
    }

    private inner class RegressionTest(
        private val dataset: Dataset,
        private val shadowJar: File,
        private val scenarioClass: Class<out Scenario>,
        private val actionCriteria: Map<ActionType<*>, Criteria>,
        private val baselineApp: AppSource,
        private val experimentApp: AppSource,
        private val duration: Duration,
        private val jiraVersion: String
    ) {
        fun run(workspace: TestWorkspace): Results {
            //provisioning
            val baselineLabel = baselineApp.getLabel()
            val baselineTest = provisioningTest(
                cohort = baselineLabel,
                jira = standalone(
                    jiraVersion,
                    dataset,
                    Apps(listOf(baselineApp))
                ),
                feature = label,
                shadowJar = shadowJar
            )
            val experimentLabel = experimentApp.getLabel()
            val experimentCohort = if (baselineLabel == experimentLabel) {
                "$experimentLabel*"
            } else {
                experimentLabel
            }

            val experimentTest = provisioningTest(
                cohort = experimentCohort,
                jira = standalone(
                    jiraVersion,
                    dataset,
                    Apps(listOf(experimentApp))
                ),
                feature = label,
                shadowJar = shadowJar
            )

            // run tests

            val executor = Executors.newFixedThreadPool(
                2,
                ThreadFactoryBuilder()
                    .setNameFormat("standalone-stability-test-thread-%d")
                    .build()
            )

            val anticipatedLoad = LoadProfile(
                loadSchedule = GrowingLoadSchedule(
                    duration = duration,
                    initialNodes = 1,
                    finalNodes = 1
                ),
                virtualUsersPerNode = 10,
                seed = 439587345
            )
            val futureBaselineResults = baselineTest.runAsync(workspace, executor, anticipatedLoad, scenarioClass)
            val futureExperimentResults = experimentTest.runAsync(workspace, executor, anticipatedLoad, scenarioClass)

            /// rest of the test

            val rawBaselineResults = futureBaselineResults.get()
            val rawExperimentResults = futureExperimentResults.get()
            executor.shutdownNow()

            val criteria = PerformanceCriteria(
                actionCriteria,
                loadProfile = anticipatedLoad
            )

            val timeline: Timeline = StandardTimeline(anticipatedLoad)
            val baselineResults = rawBaselineResults.prepareForJudgement(criteria, timeline)
            val experimentResults = rawExperimentResults.prepareForJudgement(criteria, timeline)

            return Results(
                baselineResults = baselineResults,
                experimentResults = experimentResults
            )

        }
    }

    private fun provisioningTest(
        feature: String,
        cohort: String,
        jira: JiraFormula,
        shadowJar: File
    ): ProvisioningPerformanceTest = ProvisioningPerformanceTest(
        cohort = cohort,
        infrastructureFormula = infrastructureFormula(
            feature = feature,
            jira = jira,
            shadowJar = shadowJar
        )
    )

    private fun infrastructureFormula(
        feature: String,
        jira: JiraFormula,
        shadowJar: File
    ): InfrastructureFormula<SshVirtualUsers> = InfrastructureFormula(
        investment = Investment(
            useCase = "Catch JPT regressions in $feature",
            lifespan = Duration.ofHours(1)
        ),
        jiraFormula = jira,
        virtualUsersFormula = Ec2VirtualUsersFormula(
            shadowJar = shadowJar
        ),
        aws = aws
    )

    private fun standalone(
        jiraVersion: String,
        dataset: Dataset,
        apps: Apps
    ) = StandaloneFormula(
        apps = apps,
        application = JiraSoftwareStorage(jiraVersion),
        jiraHomeSource = dataset.jiraHomeSource,
        database = dataset.database
    )
}

data class Results(val baselineResults: EdibleResult, val experimentResults: EdibleResult)
