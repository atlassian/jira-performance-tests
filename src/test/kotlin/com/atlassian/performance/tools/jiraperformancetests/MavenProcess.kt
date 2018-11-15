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
     * We don't want to hang forever, so we need a timeout.
     * It's short, because it was enough for the buffered readers to flush.
     */
    private val closeTimeout: Duration = Duration.ofSeconds(4)

    fun run(): ProcessResult = processExecutor
        .command(getMavenCommand(), *arguments.toTypedArray())
        .closeTimeout(closeTimeout.toMillis(), TimeUnit.MILLISECONDS)
        .readOutput(true)
        .redirectOutputAlsoTo(System.out)
        .execute()

    private fun getMavenCommand(): String {
        return if (isFamily(FAMILY_WINDOWS)) {
            processExecutor.directory.toPath().toAbsolutePath().resolve("mvnw.cmd").toString()
        } else {
            "./mvnw"
        }
    }
}