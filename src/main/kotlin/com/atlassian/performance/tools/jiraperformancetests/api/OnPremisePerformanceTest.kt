package com.atlassian.performance.tools.jiraperformancetests.api

import com.atlassian.performance.tools.infrastructure.api.virtualusers.LocalVirtualUsers
import com.atlassian.performance.tools.jiraactions.api.scenario.Scenario
import com.atlassian.performance.tools.report.api.HistoricalCohortsReporter
import com.atlassian.performance.tools.virtualusers.api.VirtualUserLoad
import com.atlassian.performance.tools.virtualusers.api.VirtualUserOptions
import com.atlassian.performance.tools.workspace.api.RootWorkspace
import java.net.URI
import java.time.Duration

/**
 * Gauges Jira performance, even if Jira is not accessible from the Internet.
 */
class OnPremisePerformanceTest(
    private val jira: URI
) {
    private companion object {
        private val DEFAULTS = VirtualUserOptions()
    }

    var adminLogin: String = DEFAULTS.adminLogin
    var adminPassword: String = DEFAULTS.adminPassword
    var scenario: Class<out Scenario> = DEFAULTS.scenario
    var virtualUsers: Int = DEFAULTS.virtualUserLoad.virtualUsers
    var testDuration: Duration = Duration.ofMinutes(30)
    var workspace: RootWorkspace = RootWorkspace()
    private val testName = "Benchmark my Jira"
    private val cohortName = "my-jira"

    fun run() {
        val options = VirtualUserOptions(
            adminLogin = adminLogin,
            adminPassword = adminPassword,
            scenario = scenario,
            jiraAddress = jira,
            virtualUserLoad = VirtualUserLoad(
                virtualUsers = virtualUsers,
                flat = testDuration
            ),
            diagnosticsLimit = 0
        )

        BtfJiraPerformanceMeter().run(
            virtualUsers = LocalVirtualUsers(workspace = workspace.currentTask.directory),
            virtualUserOptions = options,
            name = cohortName,
            workspace = workspace.currentTask.directory
        )

        HistoricalCohortsReporter(workspace).dump()
    }
}
