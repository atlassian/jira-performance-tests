package com.atlassian.performance.tools.jiraperformancetests.api

import com.atlassian.performance.tools.aws.api.Aws
import com.atlassian.performance.tools.awsinfrastructure.api.DatasetCatalogue
import com.atlassian.performance.tools.awsinfrastructure.api.VirtualUsersConfiguration
import com.atlassian.performance.tools.infrastructure.api.app.AppSource
import com.atlassian.performance.tools.infrastructure.api.dataset.Dataset
import com.atlassian.performance.tools.jiraactions.api.ActionType
import com.atlassian.performance.tools.jiraactions.api.scenario.Scenario
import com.atlassian.performance.tools.report.api.Criteria
import com.atlassian.performance.tools.report.api.PerformanceCriteria
import com.atlassian.performance.tools.report.api.judge.MaximumCoverageJudge
import com.atlassian.performance.tools.virtualusers.api.VirtualUserLoad
import com.atlassian.performance.tools.workspace.api.TestWorkspace
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import java.time.Duration

/**
 * High level api for plugin's tests
 */
@Deprecated(
    message = "Use AppImpactTest or use its source as a guide how to build your custom test"
)
class AppRegressionTest @JvmOverloads constructor(
    private val aws: Aws,
    private val dataset: Dataset = DatasetCatalogue().largeJira(),
    private val outputDirectory: Path = Paths.get("target"),
    duration: Duration = Duration.ofMinutes(20),
    private val deployment: AwsJiraDeployment = StandaloneAwsDeployment()
) {
    private val load = VirtualUserLoad(
        ramp = Duration.ZERO,
        flat = duration
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
            baselineApp,
            experimentApp,
            VirtualUsersConfiguration(
                scenario = scenario,
                virtualUserLoad = load
            ),
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
            virtualUserLoad = load
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