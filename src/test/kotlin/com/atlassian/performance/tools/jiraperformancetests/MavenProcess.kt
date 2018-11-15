package com.atlassian.performance.tools.jiraperformancetests

import org.codehaus.plexus.util.Os.FAMILY_WINDOWS
import org.codehaus.plexus.util.Os.isFamily
import org.zeroturnaround.exec.ProcessExecutor
import org.zeroturnaround.exec.ProcessResult
import java.time.Duration
import java.util.concurrent.TimeUnit

class MavenProcess(
    private val processExecutor: ProcessExecutor,
    private val arguments: List<String>
) {
    /**
     * We don't want to hang forever, it works now, but feel free to adjust if its causing problems.
     */
    private val closeTimeout: Duration = Duration.ofSeconds(4)

    fun run(): ProcessResult = processExecutor
        .command(getMavenCommand(), *arguments.toTypedArray())
        .closeTimeout(closeTimeout.toMillis(), TimeUnit.MILLISECONDS)
        .readOutput(true)
        .redirectOutput(System.out)
        .redirectError(System.err)
        .execute()

    private fun getMavenCommand(): String {
        return if (isFamily(FAMILY_WINDOWS)) {
            processExecutor.directory.toPath().resolve("mvnw.cmd").toString()
        } else {
            "./mvnw"
        }
    }
}