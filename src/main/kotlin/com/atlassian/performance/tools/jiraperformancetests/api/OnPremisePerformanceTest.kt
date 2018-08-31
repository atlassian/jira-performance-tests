package com.atlassian.performance.tools.jiraperformancetests.api

import com.atlassian.performance.tools.infrastructure.api.virtualusers.LocalVirtualUsers
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
    var adminLogin: String? = null
    var adminPassword: String? = null
    var virtualUsers: Int = 10
    var testDuration: Duration = Duration.ofMinutes(30)
    var workspace: RootWorkspace = RootWorkspace()
    private val testName = "Benchmark my Jira"
    private val cohortName = "my-jira"

    fun run() {
        var options = VirtualUserOptions(
            jiraAddress = jira,
            virtualUserLoad = VirtualUserLoad(
                virtualUsers = virtualUsers,
                flat = testDuration
            )
        )

        if (adminLogin != null) {
            options = options.copy(adminLogin = adminLogin!!)
        }

        if (adminPassword != null) {
            options = options.copy(adminPassword = adminPassword!!)
        }

        BtfJiraPerformanceMeter().run(
            virtualUsers = LocalVirtualUsers(workspace = workspace.currentTask.directory),
            virtualUserOptions = options,
            name = cohortName,
            workspace = workspace.currentTask.directory
        )

        HistoricalCohortsReporter(workspace).dump()
    }
}
