package com.atlassian.performance.tools.referencejiraapp.aws;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.STSAssumeRoleSessionCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.atlassian.performance.tools.aws.Aws;
import com.atlassian.performance.tools.jiraperformancetests.ExplainingAwsCredentialsProvider;

import static java.util.UUID.randomUUID;

public class MyAws {

    /**
     * Change this to authorize to your AWS account, if the @{@link DefaultAWSCredentialsProviderChain} isn't enough.
     */
    private final AWSCredentialsProvider myAwsCredentialsProvider = new STSAssumeRoleSessionCredentialsProvider.Builder(
        "arn:aws:iam::695067801333:role/JPT",
        "api-tests-" + randomUUID()
    ).build();

    public final Aws aws = new Aws(
        Regions.US_EAST_1,
        new AWSCredentialsProviderChain(
            new DefaultAWSCredentialsProviderChain(),
            myAwsCredentialsProvider,
            new ExplainingAwsCredentialsProvider(MyAws.class)
        )
    );
}
