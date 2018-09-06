package com.atlassian.performance.tools.jiraperformancetests.api

import com.atlassian.performance.tools.io.api.ensureParentDirectory
import com.atlassian.performance.tools.workspace.api.RootWorkspace
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import javax.json.Json

/**
 * Remembers the last known automatically visitable Jira.
 */
class VisitableJiraRegistry(
    workspace: RootWorkspace
) {

    private val logger: Logger = LogManager.getLogger(this::class.java)
    private val memory = workspace.directory.resolve("visitable-jira.json").toFile()

    fun memorize(
        jira: VisitableJira
    ) {
        logger.info("I'll remember how to automatically visit $jira")
        memory
            .ensureParentDirectory()
            .writeText(jira.toJson())
    }

    fun recall(): VisitableJira? {
        return if (memory.exists()) {
            VisitableJira(
                json = Json.createReader(memory.reader()).readObject()
            )
        } else {
            logger.info("I don't remember a Jira you can automatically visit. I checked in $memory .")
            null
        }
    }
}