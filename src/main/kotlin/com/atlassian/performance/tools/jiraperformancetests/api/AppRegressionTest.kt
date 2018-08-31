package com.atlassian.performance.tools.jiraperformancetests.api

import com.atlassian.performance.tools.aws.Aws
import com.atlassian.performance.tools.awsinfrastructure.DatasetCatalogue
import com.atlassian.performance.tools.infrastructure.api.app.AppSource
import com.atlassian.performance.tools.infrastructure.api.dataset.Dataset
import com.atlassian.performance.tools.infrastructure.api.virtualusers.GrowingLoadSchedule
import com.atlassian.performance.tools.infrastructure.api.virtualusers.LoadProfile
import com.atlassian.performance.tools.jiraactions.ActionType
import com.atlassian.performance.tools.jiraactions.scenario.Scenario
import com.atlassian.performance.tools.report.api.Criteria
import com.atlassian.performance.tools.report.api.PerformanceCriteria
import com.atlassian.performance.tools.report.api.judge.MaximumCoverageJudge
import com.atlassian.performance.tools.workspace.api.TestWorkspace
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import java.time.Duration

/**
 * High level api for plugin's tests
 */
class AppRegressionTest @JvmOverloads constructor(
    private val aws: Aws,
    private val dataset: Dataset = DatasetCatalogue().largeJira(),
    private val outputDirectory: Path = Paths.get("target"),
    duration: Duration = Duration.ofMinutes(20),
    private val deployment: AwsJiraDeployment = StandaloneAwsDeployment()
) {
    private val load = LoadProfile(
        loadSchedule = GrowingLoadSchedule(
            duration = duration,
            initialNodes = 1,
            finalNodes = 1
        ),
        virtualUsersPerNode = 10,
        seed = 439587345
    )

    @JvmOverloads
    fun run(
        testJar: File,
        scenario: Class<out Scenario>,
        experimentApp: AppSource,
        baselineApp: AppSource,
        jiraVersion: String = "7.5.0"
    ): RegressionResults {
        val pluginTester = AwsPluginTester(
            aws = aws,
            dataset = dataset,
            outputDirectory = outputDirectory,
            deployment = deployment
        )

        return pluginTester.run(
            testJar,
            scenario,
            baselineApp,
            experimentApp,
            load,
            jiraVersion
        )
    }

    fun assertNoRegression(
        results: RegressionResults,
        criteria: Map<ActionType<*>, Criteria>
    ) {
        val workspace = outputDirectory.resolve("surefire-reports")
        val performanceCriteria = PerformanceCriteria(
            actionCriteria = criteria,
            loadProfile = load
        )
        val verdict = MaximumCoverageJudge().judge(
            baseline = results.baseline,
            experiment = results.experiment,
            criteria = performanceCriteria,
            report = TestWorkspace(workspace)
        )
        verdict.assertAccepted(
            this.javaClass.canonicalName,
            workspace
        )
    }
}