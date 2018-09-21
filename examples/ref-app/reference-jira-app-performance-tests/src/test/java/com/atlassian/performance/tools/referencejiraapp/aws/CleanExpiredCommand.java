package com.atlassian.performance.tools.referencejiraapp.aws;

import com.atlassian.performance.tools.jiraperformancetests.api.AwsHousekeeping;

public class CleanExpiredCommand {

    /**
     * This will clean all expired stacks and instances for all users.
     * You can run it manually from IDE or from command line in the examples/ref-app directory:
     * ./mvnw -pl reference-jira-app-performance-tests test-compile exec:java@clean-all-expired
     *
     * Example output:
     * ```
     * 13:46:13,520 INFO  {: } Cleaning expired resources...
     * 13:46:14,940 INFO  {: } All expired resources have been cleaned
     * ```
     */
    public static void main(String[] args) {
        new AwsHousekeeping(new MyAws().aws).cleanAllExpired();
    }

}
