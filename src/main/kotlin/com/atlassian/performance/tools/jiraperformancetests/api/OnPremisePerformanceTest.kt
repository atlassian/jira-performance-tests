package com.atlassian.performance.tools.jiraperformancetests.api

import com.atlassian.performance.tools.infrastructure.api.virtualusers.LocalVirtualUsers
import com.atlassian.performance.tools.jiraactions.api.parser.MergingActionMetricsParser
import com.atlassian.performance.tools.jiraactions.api.scenario.Scenario
import com.atlassian.performance.tools.jirasoftwareactions.api.JiraSoftwareScenario
import com.atlassian.performance.tools.report.api.FullReport
import com.atlassian.performance.tools.report.api.HistoricalCohortsReporter
import com.atlassian.performance.tools.report.api.StandardTimeline
import com.atlassian.performance.tools.report.api.parser.MergingNodeCountParser
import com.atlassian.performance.tools.report.api.parser.SystemMetricsParser
import com.atlassian.performance.tools.report.api.result.FullCohortResult
import com.atlassian.performance.tools.virtualusers.api.VirtualUserLoad
import com.atlassian.performance.tools.virtualusers.api.VirtualUserOptions
import com.atlassian.performance.tools.virtualusers.api.browsers.Browser
import com.atlassian.performance.tools.virtualusers.api.browsers.HeadlessChromeBrowser
import com.atlassian.performance.tools.virtualusers.api.config.VirtualUserBehavior
import com.atlassian.performance.tools.virtualusers.api.config.VirtualUserTarget
import com.atlassian.performance.tools.workspace.api.RootWorkspace
import com.atlassian.performance.tools.workspace.api.TestWorkspace
import java.net.URI
import java.time.Duration
import java.util.*

/**
 * Gauges Jira performance, even if Jira is not accessible from the Internet.
 */
class OnPremisePerformanceTest(
    private val jira: URI
) {

    var adminLogin: String = "admin"
    var adminPassword: String = "admin"
    var scenario: Class<out Scenario> = JiraSoftwareScenario::class.java
    var browser: Class<out Browser> = HeadlessChromeBrowser::class.java
    var virtualUsers: Int = 10
    var testDuration: Duration = Duration.ofMinutes(30)
    var workspace: RootWorkspace = RootWorkspace()
    private val cohortName = "my-jira"

    fun run() {
        val virtualUserBehavior = VirtualUserBehavior(
            load = VirtualUserLoad(
                virtualUsers = virtualUsers,
                flat = testDuration
            ),
            browser = browser,
            scenario = scenario,
            diagnosticsLimit = 0,
            seed = Random().nextLong()
        )
        val virtualUserTarget = VirtualUserTarget(
            webApplication = jira,
            userName = adminLogin,
            password = adminPassword
        )
        val options = VirtualUserOptions(
            virtualUserTarget,
            virtualUserBehavior
        )

        val localVirtualUsers = LocalVirtualUsers(workspace = workspace.currentTask.directory)

        try {
            localVirtualUsers.applyLoad(options)
        } finally {
            localVirtualUsers.gatherResults()
        }
        val result = FullCohortResult(
            cohort = cohortName,
            results = workspace.currentTask.directory,
            actionParser = MergingActionMetricsParser(),
            systemParser = SystemMetricsParser(),
            nodeParser = MergingNodeCountParser()
        ).prepareForJudgement(
            timeline = StandardTimeline(options.behavior.load.total)
        )
        FullReport().dump(
            results = listOf(result),
            workspace = TestWorkspace(workspace.currentTask.directory)
        )

        HistoricalCohortsReporter(workspace).dump()
    }
}
