package com.atlassian.performance.tools.referencejiraapp;

import com.atlassian.performance.tools.infrastructure.api.app.AppSource;
import com.atlassian.performance.tools.jiraperformancetests.api.AppImpactTest;
import com.atlassian.performance.tools.jiraperformancetests.api.LocalApp;
import com.atlassian.performance.tools.referencejiraapp.aws.MyAws;
import org.junit.Test;

import java.io.File;

public class SlowEventListenerPerformanceIT {

    @Test
    public void shouldNotSlowJiraDown() {
        final AppSource app = new LocalApp(
            new File("../reference-jira-app/target/reference-jira-app-1.0-SNAPSHOT.obr")
        );
        final File virtualUsersJar = new File("target/reference-jira-app-performance-tests-1.0-SNAPSHOT-fat-tests.jar");
        final AppImpactTest test = new AppImpactTest(app, new MyAws().aws, virtualUsersJar);

//        /*
//         * Optionally, express your performance impact expectations here. Uncomment it and fix imports if you wish.
//         */
//        final java.util.Map<
//                com.atlassian.performance.tools.jiraactions.api.ActionType<?>,
//                com.atlassian.performance.tools.report.api.Criteria
//                > criteria =
//                com.google.common.collect.ImmutableMap.of(
//                        com.atlassian.performance.tools.jiraactions.api.ActionTypes.CREATE_ISSUE_SUBMIT,
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
//                com.atlassian.performance.tools.jiraactions.api.ActionType<?>,
//                com.atlassian.performance.tools.report.api.Criteria
//                > criteria =
//                com.google.common.collect.ImmutableMap.of(
//                        com.atlassian.performance.tools.jiraactions.api.ActionTypes.CREATE_ISSUE_SUBMIT,
//                        new com.atlassian.performance.tools.report.api.Criteria(
//                                0.20f,
//                                12,
//                                3
//                        ),
//                        com.atlassian.performance.tools.jiraactions.api.ActionTypes.EDIT_ISSUE,
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