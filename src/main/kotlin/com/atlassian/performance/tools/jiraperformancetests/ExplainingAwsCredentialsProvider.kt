package com.atlassian.performance.tools.jiraperformancetests

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSCredentialsProvider
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class ExplainingAwsCredentialsProvider(
    private val classToCustomize: Class<*>
) : AWSCredentialsProvider {

    private val logger: Logger = LogManager.getLogger(this::class.java)

    override fun getCredentials(): AWSCredentials {
        """
        ****************************************************************
        You need to set up AWS credentials in $classToCustomize
        ****************************************************************
        """
            .trimIndent()
            .lines()
            .forEach { logger.error(it) }
        throw Exception("We can only explain how you can supply the credentials")
    }

    override fun refresh() {
    }
}