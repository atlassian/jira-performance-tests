package com.atlassian.performance.tools.jiraperformancetests

import org.codehaus.plexus.util.Os
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
        .command(
            if (Os.isFamily(Os.FAMILY_WINDOWS)) "mvnw.cmd" else "./mvnw",
            *arguments.toTypedArray()
        )
        .closeTimeout(closeTimeout.toMillis(), TimeUnit.MILLISECONDS)
        .readOutput(true)
        .redirectOutput(System.out)
        .redirectError(System.err)
        .execute()
}