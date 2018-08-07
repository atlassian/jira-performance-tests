package com.atlassian.performance.tools.jiraperformancetests

import com.atlassian.performance.tools.infrastructure.virtualusers.LoadProfile
import com.atlassian.performance.tools.infrastructure.virtualusers.VirtualUsers
import com.atlassian.performance.tools.jiraactions.MergingActionMetricsParser
import com.atlassian.performance.tools.jirasoftwareactions.JiraSoftwareScenario
import com.atlassian.performance.tools.report.*
import com.atlassian.performance.tools.workspace.git.GitRepo
import java.net.URI
import java.nio.file.Path

/**
 * Gauges Jira performance, even if Jira is not accessible from the Internet.
 */
class BtfJiraPerformanceMeter {

    /**
     * Runs the test
     *
     * @param jira instance address to run the test against
     * @param virtualUsers to be used for the test
     * @param loadProfile configuration for the test
     * @param name of the test
     * @param results location
     */
    fun run(
        jira: URI,
        virtualUsers: VirtualUsers,
        loadProfile: LoadProfile,
        name: String,
        results: Path
    ) {
        try {
            virtualUsers.applyLoad(jira, loadProfile, JiraSoftwareScenario::class.java)
        } finally {
            virtualUsers.gatherResults()
        }

        val cohortResults = FullCohortResult(
            cohort = name,
            results = results,
            actionParser = MergingActionMetricsParser(),
            systemParser = SystemMetricsParser(),
            nodeParser = MergingNodeCountParser()
        )

        val edibleResult = cohortResults
            .prepareForJudgement(
                PerformanceCriteria(emptyMap(), loadProfile),
                StandardTimeline(loadProfile)
            )

        TimelineChart(GitRepo.findFromCurrentDirectory()).generate(
            output = results.resolve("time-series-chart.html"),
            actionMetrics = edibleResult.allActionMetrics,
            systemMetrics = edibleResult.systemMetrics
        )
    }
}