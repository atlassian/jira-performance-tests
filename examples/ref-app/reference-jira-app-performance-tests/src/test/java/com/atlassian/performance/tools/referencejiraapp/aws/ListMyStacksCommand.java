package com.atlassian.performance.tools.referencejiraapp.aws;

import com.atlassian.performance.tools.jiraperformancetests.api.AwsHousekeeping;

public class ListMyStacksCommand {

    /**
     * This will list all AWS stacks provisioned by the current user.
     * You can run it manually from IDE or from command line in the examples/ref-app directory:
     * ./mvnw -pl reference-jira-app-performance-tests test-compile exec:java@list-my-stacks
     *
     * Example output:
     * ```
     * 13:42:07,556 INFO  {: } Below is the list of AWS stacks that were provisioned by the current user:
     * 13:42:07,558 INFO  {: } johndoe: ListedStack(name=jpt-ad772592-1ded-474e-9712-2cb8882f775d-d2f52ac1, user=johndoe, timeLeft=PT59M51.072S, status=CREATE_IN_PROGRESS)
     * 13:42:07,558 INFO  {: } johndoe: ListedStack(name=jpt-b9dec4c0-ab09-4283-a6f8-59b6a1ff2c98-cb80e05a, user=johndoe, timeLeft=PT59M50.92S, status=CREATE_IN_PROGRESS)
     * ```
     */
    public static void main(String[] args) {
        new AwsHousekeeping(new MyAws().aws).listMyStacks();
    }

}
