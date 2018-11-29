package com.atlassian.performance.tools.jiraperformancetests.api

import com.atlassian.performance.tools.awsinfrastructure.api.hardware.C5NineExtraLargeEphemeral
import com.atlassian.performance.tools.awsinfrastructure.api.jira.DataCenterFormula
import com.atlassian.performance.tools.awsinfrastructure.api.jira.JiraFormula
import com.atlassian.performance.tools.awsinfrastructure.api.jira.StandaloneFormula
import com.atlassian.performance.tools.awsinfrastructure.api.loadbalancer.ElasticLoadBalancerFormula
import com.atlassian.performance.tools.awsinfrastructure.api.storage.ApplicationStorage
import com.atlassian.performance.tools.infrastructure.api.app.Apps
import com.atlassian.performance.tools.infrastructure.api.database.Database
import com.atlassian.performance.tools.infrastructure.api.jira.JiraHomeSource
import com.atlassian.performance.tools.infrastructure.api.jira.JiraJvmArgs
import com.atlassian.performance.tools.infrastructure.api.jira.JiraLaunchTimeouts
import com.atlassian.performance.tools.infrastructure.api.jira.JiraNodeConfig
import java.time.Duration

interface AwsJiraDeployment {

    fun createJiraFormula(
        apps: Apps,
        application: ApplicationStorage,
        jiraHomeSource: JiraHomeSource,
        database: Database
    ): JiraFormula
}

class StandaloneAwsDeployment : AwsJiraDeployment {
    override fun createJiraFormula(
        apps: Apps,
        application: ApplicationStorage,
        jiraHomeSource: JiraHomeSource,
        database: Database
    ): JiraFormula = StandaloneFormula(
        apps = apps,
        application = application,
        jiraHomeSource = jiraHomeSource,
        database = database
    )
}

class DataCenterAwsDeployment(
    private val nodes: Int = 2
) : AwsJiraDeployment {
    override fun createJiraFormula(
        apps: Apps,
        application: ApplicationStorage,
        jiraHomeSource: JiraHomeSource,
        database: Database
    ): JiraFormula = DataCenterFormula(
        configs = JiraNodeConfig(
            name = "jira-node",
            jvmArgs = JiraJvmArgs(),
            launchTimeouts = JiraLaunchTimeouts(
                offlineTimeout = Duration.ofMinutes(8),
                initTimeout = Duration.ofMinutes(4),
                upgradeTimeout = Duration.ofMinutes(8),
                unresponsivenessTimeout = Duration.ofMinutes(4)
            )
        ).clone(nodes),
        loadBalancerFormula = ElasticLoadBalancerFormula(),
        apps = apps,
        application = application,
        jiraHomeSource = jiraHomeSource,
        database = database,
        computer = C5NineExtraLargeEphemeral()
    )
}