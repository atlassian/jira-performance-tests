package com.atlassian.performance.tools.referencejiraapp.aws;

import com.atlassian.performance.tools.jiraperformancetests.api.AwsHousekeeping;

public class DeleteMyStacksCommand {

    /**
     * This will delete all AWS stacks provisioned by the current user.
     * You can run it manually from IDE or from command line in the examples/ref-app directory:
     * ./mvnw -pl reference-jira-app-performance-tests test-compile exec:java@delete-my-stacks
     *
     * Example output:
     * ```
     * 13:46:37,826 INFO  {: } Retrieving stacks for the current user...
     * 13:46:38,186 INFO  {: } Deleting stacks...
     * 13:46:38,540 INFO  {: } Deleted jpt-ad772592-1ded-474e-9712-2cb8882f775d-d2f52ac1
     * 13:46:38,695 INFO  {: } Deleted jpt-b9dec4c0-ab09-4283-a6f8-59b6a1ff2c98-cb80e05a
     * 13:46:38,696 INFO  {: } All stacks of the current user have been deleted
     * ```
     */
    public static void main(String[] args) {
        new AwsHousekeeping(new MyAws().aws).deleteMyStacks();
    }

}
