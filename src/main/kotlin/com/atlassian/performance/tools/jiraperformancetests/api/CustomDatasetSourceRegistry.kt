package com.atlassian.performance.tools.jiraperformancetests.api

import com.atlassian.performance.tools.awsinfrastructure.api.CustomDatasetSource
import com.atlassian.performance.tools.awsinfrastructure.api.Infrastructure
import com.atlassian.performance.tools.io.api.ensureParentDirectory
import com.atlassian.performance.tools.workspace.api.RootWorkspace
import com.atlassian.performance.tools.workspace.api.TaskWorkspace
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.attribute.FileTime
import javax.json.Json

class CustomDatasetSourceRegistry(
    private val workspace: RootWorkspace
) {
    private val logger: Logger = LogManager.getLogger(this::class.java)

    private fun jsonFile(
        task: TaskWorkspace
    ): File = task
        .directory
        .resolve("dataset-source.json")
        .toFile()

    fun register(
        infrastructure: Infrastructure<*>
    ): CustomDatasetSource {
        val datasetSource = CustomDatasetSource(
            infrastructure.jira.jiraHome,
            infrastructure.jira.database!!
        )
        logger.info("Registering dataset source")
        jsonFile(workspace.currentTask)
            .ensureParentDirectory()
            .writeText(datasetSource.toJson().toString())
        return datasetSource
    }

    fun load(): CustomDatasetSource? = workspace
        .listTasks()
        .sortedByDescending { it.directory.getCreationTime() }
        .asSequence()
        .mapNotNull { extractSource(it) }
        .firstOrNull()

    private fun Path.getCreationTime(): FileTime = Files.readAttributes(
        this,
        BasicFileAttributes::class.java
    ).creationTime()

    private fun extractSource(
        task: TaskWorkspace
    ): CustomDatasetSource? = try {
        val fileReader = jsonFile(task).reader()
        val json = Json.createReader(fileReader).readObject()
        val source = CustomDatasetSource(json)
        logger.info("Found a custom dataset source in ${task.directory}")
        source
    } catch (e: Exception) {
        logger.debug("Failed to extract a custom dataset source from $task", e)
        null
    }
}