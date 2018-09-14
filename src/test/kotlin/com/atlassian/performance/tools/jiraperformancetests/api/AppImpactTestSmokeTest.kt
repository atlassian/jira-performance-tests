package com.atlassian.performance.tools.jiraperformancetests.api

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.regions.Regions
import com.amazonaws.services.support.AWSSupportClientBuilder
import com.atlassian.performance.tools.aws.api.Aws
import com.atlassian.performance.tools.aws.api.SupportCapacityMediator
import com.atlassian.performance.tools.infrastructure.api.app.AppSource
import com.atlassian.performance.tools.io.api.dereference
import com.atlassian.performance.tools.jirasoftwareactions.api.JiraSoftwareScenario
import org.apache.http.message.BasicHeaderValueParser.INSTANCE
import org.apache.http.message.BasicHeaderValueParser.parseHeaderElement
import org.junit.Test
import java.io.File
import java.net.URI
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.time.Duration

class AppImpactTestSmokeTest {

    /**
     * Don't bump the timeout. If it times out, make it quicker.
     */
    @Test(timeout = 27 * 60 * 1000L)
    fun shouldRun() {
        val region = Regions.US_EAST_1
        val credentialsProvider = DefaultAWSCredentialsProviderChain()
        val support = AWSSupportClientBuilder.standard()
            .withCredentials(credentialsProvider)
            .withRegion(region)
            .build()
        val test = AppImpactTest(
            app = MarketplaceUriApp(URI("https://marketplace.atlassian.com/download/apps/1217110/version/1000051")),
            aws = Aws(
                region = region,
                credentialsProvider = credentialsProvider,
                capacity = SupportCapacityMediator(
                    support = support,
                    region = region
                )
            ),
            testJar = dereference("jpt.virtual-users.shadow-jar")
        )
        test.scenario = JiraSoftwareScenario::class.java
        test.duration = Duration.ofMinutes(1)

        test.run()
    }
}

// Consider moving to `aws-infrastructure`.
private class MarketplaceUriApp(
    private val uri: URI
) : AppSource {

    override fun acquireFiles(
        directory: File
    ): List<File> = listOf(downloadToDirectory(directory))

    private fun downloadToDirectory(
        directory: File
    ): File {
        val url = uri.toURL()
        val connection = url.openConnection()
        connection.connect()
        val contentDisposition = connection.getHeaderField("Content-Disposition")!!
        val attachmentFilename = parseHeaderElement(contentDisposition, INSTANCE)
            .getParameterByName("filename")
            .value
        val file = directory.resolve(attachmentFilename)
        url.openStream().use { Files.copy(it, file.toPath(), StandardCopyOption.REPLACE_EXISTING) }
        return file
    }

    override fun getLabel(): String = uri.toString()
}
