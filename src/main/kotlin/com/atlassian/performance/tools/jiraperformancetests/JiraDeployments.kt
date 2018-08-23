package com.atlassian.performance.tools.jiraperformancetests

import com.atlassian.performance.tools.awsinfrastructure.jira.DataCenterFormula
import com.atlassian.performance.tools.awsinfrastructure.jira.JiraFormula
import com.atlassian.performance.tools.awsinfrastructure.jira.StandaloneFormula
import com.atlassian.performance.tools.awsinfrastructure.storage.ApplicationStorage
import com.atlassian.performance.tools.infrastructure.Database
import com.atlassian.performance.tools.infrastructure.app.Apps
import com.atlassian.performance.tools.infrastructure.jira.home.JiraHomeSource
import com.atlassian.performance.tools.infrastructure.jira.nodes.JiraNodeConfig

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
        configs = JiraNodeConfig().clone(nodes),
        apps = apps,
        application = application,
        jiraHomeSource = jiraHomeSource,
        database = database
    )
}