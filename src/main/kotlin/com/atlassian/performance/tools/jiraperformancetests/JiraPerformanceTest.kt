package com.atlassian.performance.tools.jiraperformancetests

import com.atlassian.performance.tools.aws.Aws
import com.atlassian.performance.tools.awsinfrastructure.DatasetCatalogue
import com.atlassian.performance.tools.infrastructure.Dataset
import com.atlassian.performance.tools.infrastructure.app.AppSource
import com.atlassian.performance.tools.jiraactions.ActionType
import com.atlassian.performance.tools.jiraactions.scenario.Scenario
import com.atlassian.performance.tools.report.BaselineComparingJudge
import com.atlassian.performance.tools.report.Criteria
import com.atlassian.performance.tools.report.IndependentCohortsJudge
import com.atlassian.performance.tools.workspace.TestWorkspace
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import java.time.Duration

/**
 * High level api for plugin's tests
 */
class JiraPerformanceTest @JvmOverloads constructor(
    private val aws: Aws,
    private val dataset: Dataset = DatasetCatalogue().largeJira(),
    private val outputDirectory: Path = Paths.get("target")
) {
    @JvmOverloads
    fun runRegressionTest(
        testJar: File,
        scenario: Class<out Scenario>,
        criteria: Map<ActionType<*>, Criteria>,
        experimentApp: AppSource,
        baselineApp: AppSource,
        jiraVersion: String = "7.5.0",
        duration: Duration = Duration.ofMinutes(20)
    ): Results {
        val pluginTester = AwsPluginTester(aws, dataset, outputDirectory)

        return pluginTester.run(
            testJar,
            scenario,
            criteria,
            baselineApp,
            experimentApp,
            duration,
            jiraVersion
        )
    }

    fun assertNoRegression(results: Results) {
        val workspace = outputDirectory.resolve("surefire-reports")
        val verdict = IndependentCohortsJudge().judge(
            results = listOf(results.baselineResults, results.experimentResults),
            report = TestWorkspace(workspace)
        ) + BaselineComparingJudge().judge(
            baseline = results.baselineResults,
            experiment = results.experimentResults
        )
        verdict.assertAccepted(
            this.javaClass.canonicalName,
            workspace,
            12
        )
    }
}