package com.atlassian.performance.tools.jiraperformancetests

import com.atlassian.performance.tools.aws.Aws
import com.atlassian.performance.tools.awsinfrastructure.DatasetCatalogue
import com.atlassian.performance.tools.infrastructure.Dataset
import com.atlassian.performance.tools.infrastructure.app.AppSource
import com.atlassian.performance.tools.jiraactions.ActionType
import com.atlassian.performance.tools.jiraactions.scenario.Scenario
import com.atlassian.performance.tools.report.Criteria
import com.atlassian.performance.tools.report.RelativeTypicalPerformanceJudge
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
        RelativeTypicalPerformanceJudge()
            .judge(
                results.baselineResults.criteria.getCenterCriteria(),
                results.baselineResults.actionStats,
                results.experimentResults.actionStats
            ).assertAccepted(
                this.javaClass.canonicalName,
                outputDirectory.resolve("surefire-reports"),
                1
            )
    }
}