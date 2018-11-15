package com.atlassian.performance.tools.jiraperformancetests.api

import com.atlassian.performance.tools.jiraperformancetests.AcceptanceCategory
import com.atlassian.performance.tools.jiraperformancetests.MavenProcess
import com.atlassian.performance.tools.jiraperformancetests.SystemProperty
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.experimental.categories.Category
import org.zeroturnaround.exec.ProcessExecutor
import java.nio.file.Paths
import java.util.concurrent.TimeUnit

class CustomerJourneyTest {

    @Test
    @Category(AcceptanceCategory::class)
    fun shouldLinkLibrariesInBtfExample() {
        val jptVersion: String = SystemProperty("jpt.version").dereference()
        val mavenProcess = MavenProcess(
            processExecutor = ProcessExecutor()
                .directory(Paths.get("examples", "btf-test").toFile())
                .timeout(7, TimeUnit.MINUTES),
            arguments = listOf("verify", "-Djpt.version=$jptVersion")
        )
        Thread.sleep(10000) // Work around https://github.com/takari/maven-wrapper/issues/93

        val result = mavenProcess.run()

        val testsHeader = "[INFO]  T E S T S"
        assertThat(result.output.lines)
            .`as`("the example should reach the tests")
            .contains(testsHeader)
        val outputAfterTests = result.output.lines.dropWhile { it != testsHeader }
        assertThat(outputAfterTests)
            .`as`("the example should link libraries successfully")
            .noneMatch { it.contains("NoSuchMethodError") }
    }
}
