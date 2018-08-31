package com.atlassian.performance.tools.jiraperformancetests.api

import com.atlassian.performance.tools.infrastructure.api.virtualusers.LoadProfile
import com.atlassian.performance.tools.infrastructure.api.virtualusers.VirtualUsers
import com.atlassian.performance.tools.jiraactions.MergingActionMetricsParser
import com.atlassian.performance.tools.jirasoftwareactions.JiraSoftwareScenario
import com.atlassian.performance.tools.report.api.FullReport
import com.atlassian.performance.tools.report.api.StandardTimeline
import com.atlassian.performance.tools.report.api.parser.MergingNodeCountParser
import com.atlassian.performance.tools.report.api.parser.SystemMetricsParser
import com.atlassian.performance.tools.report.api.result.FullCohortResult
import com.atlassian.performance.tools.workspace.api.TestWorkspace
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
     * @param workspace location
     */
    fun run(
        jira: URI,
        virtualUsers: VirtualUsers,
        loadProfile: LoadProfile,
        name: String,
        workspace: Path
    ) {
        try {
            virtualUsers.applyLoad(jira, loadProfile, JiraSoftwareScenario::class.java)
        } finally {
            virtualUsers.gatherResults()
        }
        val result = FullCohortResult(
            cohort = name,
            results = workspace,
            actionParser = MergingActionMetricsParser(),
            systemParser = SystemMetricsParser(),
            nodeParser = MergingNodeCountParser()
        ).prepareForJudgement(
            timeline = StandardTimeline(loadProfile)
        )
        FullReport().dump(
            results = listOf(result),
            workspace = TestWorkspace(workspace)
        )
    }
}