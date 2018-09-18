package com.atlassian.performance.tools.jiraperformancetests.api

import com.atlassian.performance.tools.infrastructure.api.app.AppSource
import java.io.File

/**
 * Points to a pre-built local app.
 */
class LocalApp(
    private val app: File
) : AppSource {
    override fun acquireFiles(directory: File): List<File> = listOf(app)
    override fun getLabel(): String = app.name
}
