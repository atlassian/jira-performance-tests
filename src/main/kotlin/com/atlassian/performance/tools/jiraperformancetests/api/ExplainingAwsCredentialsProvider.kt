package com.atlassian.performance.tools.jiraperformancetests.api

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class ExplainingAwsCredentialsProvider(
    private val classToCustomize: Class<*>
) : AWSCredentialsProvider {

    private val logger: Logger = LogManager.getLogger(this::class.java)

    override fun getCredentials(): AWSCredentials {
        """
        You need to provide AWS credentials to run the test.
        By default we use ${DefaultAWSCredentialsProviderChain::class.java}.

        AWS credentials provider chain that looks for credentials in this order:
         * Environment Variables - AWS_ACCESS_KEY_ID and AWS_SECRET_ACCESS_KEY
         * Java System Properties - aws.accessKeyId and aws.secretKey
         * Credential profiles file at the default location (~/.aws/credentials) shared by all AWS SDKs and the AWS CLI
         * Credentials delivered through the Amazon EC2 container service if AWS_CONTAINER_CREDENTIALS_RELATIVE_URI"
          environment variable is set and security manager has permission to access the variable,
         * Instance profile credentials delivered through the Amazon EC2 metadata service


        JPT by default uses Regions.US_EAST_1.
        ****************************************************************
        You can also customise AWS credential providers and region in
        class $classToCustomize.
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