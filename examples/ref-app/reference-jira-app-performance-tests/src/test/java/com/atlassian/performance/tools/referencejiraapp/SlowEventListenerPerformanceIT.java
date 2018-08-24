package com.atlassian.performance.tools.referencejiraapp;

import com.amazonaws.auth.STSAssumeRoleSessionCredentialsProvider.Builder;
import com.atlassian.performance.tools.aws.Aws;
import com.atlassian.performance.tools.infrastructure.api.app.MavenApp;
import com.atlassian.performance.tools.jiraperformancetests.AppImpactTest;
import org.junit.Test;

import static com.amazonaws.regions.Regions.EU_WEST_1;
import static java.util.UUID.randomUUID;

public class SlowEventListenerPerformanceIT {

    @Test
    public void shouldNotSlowJiraDown() {
        // Point this towards your app.
        final MavenApp app = new MavenApp(
                "com.atlassian.performance.tools",
                "reference-jira-app",
                "1.0-SNAPSHOT"
        );
        // Point this towards your AWS setup.
        final Aws aws = new Aws(
                EU_WEST_1,
                new Builder(
                        "arn:aws:iam::695067801333:role/JPT",
                        "api-tests-" + randomUUID()
                ).build()
        );
        final AppImpactTest test = new AppImpactTest(app, aws);

//        /*
//         * Optionally, express your performance impact expectations here. Uncomment it and fix imports if you wish.
//         */
//        final java.util.Map<
//                com.atlassian.performance.tools.jiraactions.ActionType<?>,
//                com.atlassian.performance.tools.report.api.Criteria
//                > criteria =
//                com.google.common.collect.ImmutableMap.of(
//                        com.atlassian.performance.tools.jiraactions.ActionTypes.CREATE_ISSUE_SUBMIT,
//                        new com.atlassian.performance.tools.report.api.Criteria(
//                                0.20f,
//                                12,
//                                3
//                        )
//                );
//        test.setCriteria(criteria);

//        /*
//         * More complex expectations can be expressed by adding new entries to the map like below:
//         */
//        final java.util.Map<
//                com.atlassian.performance.tools.jiraactions.ActionType<?>,
//                com.atlassian.performance.tools.report.api.Criteria
//                > criteria =
//                com.google.common.collect.ImmutableMap.of(
//                        com.atlassian.performance.tools.jiraactions.ActionTypes.CREATE_ISSUE_SUBMIT,
//                        new com.atlassian.performance.tools.report.api.Criteria(
//                                0.20f,
//                                12,
//                                3
//                        ),
//                        com.atlassian.performance.tools.jiraactions.ActionTypes.EDIT_ISSUE,
//                        new com.atlassian.performance.tools.report.api.Criteria(
//                                0.30f,
//                                5,
//                                1
//                        )
//
//                );
//        test.setCriteria(criteria);

//        /*
//         * Optionally, pass in your custom user scenario. The default user scenario might not cover
//         * interactions with your app.
//         */
//        test.setScenario(com.atlassian.performance.tools.referencejiraapp.scenario.MyScenario.class);

//        /*
//         * Optionally, set Jira version to test against.
//         */
//        test.setJiraVersion("7.2.12");

//        /*
//         * Optionally, change the duration of the test to suit your needs.
//         */
//        test.setDuration(java.time.Duration.ofMinutes(15));

//        /*
//         * Optionally, set the location of the JAR file with the tests.
//         */
//        test.setTestJar(new java.io.File("/path/to/custom/jar/file"));

        test.run();
    }
}