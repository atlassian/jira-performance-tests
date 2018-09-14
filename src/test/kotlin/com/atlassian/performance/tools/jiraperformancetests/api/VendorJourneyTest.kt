package com.atlassian.performance.tools.jiraperformancetests.api

import com.atlassian.performance.tools.jiraperformancetests.AcceptanceCategory
import org.assertj.core.api.Assertions.assertThat
import org.codehaus.plexus.util.Os.FAMILY_WINDOWS
import org.codehaus.plexus.util.Os.isFamily
import org.junit.Test
import org.junit.experimental.categories.Category
import java.nio.file.Paths
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class VendorJourneyTest {

    @Test
    @Category(AcceptanceCategory::class)
    fun shouldRunRefApp() {
        val mvnCommand = if (isFamily(FAMILY_WINDOWS)) "mvnw.cmd" else "./mvnw"
        val mavenPackage = ProcessBuilder(
            mvnCommand,
            "install"
        ).directory(
            Paths.get("examples", "ref-app").toFile()
        )

        val process = mavenPackage.start()

        val outputLines = mutableListOf<String>()
        Executors.newSingleThreadExecutor().submit { printAndGatherOutput(process, outputLines) }
        val timedOut = process.waitFor(55, TimeUnit.MINUTES).not()
        process.destroy()
        val lastFewLinesOfOutput = outputLines.takeLast(12).joinToString(separator = "\n")
        assertThat(timedOut)
            .`as`("time out")
            .isFalse()
        assertThat(lastFewLinesOfOutput)
            .`as`("last few lines of output")
            .contains("BUILD SUCCESS")
    }

    private fun printAndGatherOutput(
        process: Process,
        outputLines: MutableList<String>
    ) {
        process
            .inputStream
            .use { stream ->
                stream
                    .bufferedReader()
                    .lineSequence()
                    .forEach { line ->
                        outputLines += line
                        println(line)
                    }
            }
    }
}
