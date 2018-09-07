package com.atlassian.performance.tools.jiraperformancetests.api

import org.assertj.core.api.Assertions.assertThat
import org.codehaus.plexus.util.Os.FAMILY_WINDOWS
import org.codehaus.plexus.util.Os.isFamily
import org.junit.Test
import java.nio.file.Paths
import java.util.concurrent.TimeUnit

class VendorJourneyTest {

    @Test
    fun shouldRunRefApp() {
        val mvnCommand = if (isFamily(FAMILY_WINDOWS)) "mvnw.cmd" else "./mvnw"
        val mavenPackage = ProcessBuilder(
            mvnCommand,
            "install"
        ).directory(
            Paths.get("examples", "ref-app").toFile()
        )

        val process = mavenPackage.start()
        val output = printAndGatherOutput(process)
        process.waitFor(55, TimeUnit.MINUTES)
        assertThat(output).contains("BUILD SUCCESS")
    }

    private fun printAndGatherOutput(process: Process): String {
        val output = mutableListOf<String>()
        process
            .inputStream
            .use { stream ->
                stream
                    .bufferedReader()
                    .lines()
                    .forEach { line ->
                        output += line
                        println(line)
                    }
            }
        return output.joinToString("\n")
    }
}