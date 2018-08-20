package com.atlassian.performance.tools.referencejiraapp;

import com.amazonaws.auth.STSAssumeRoleSessionCredentialsProvider.Builder;
import com.atlassian.performance.tools.referencejiraapp.scenario.MyScenario;
import com.atlassian.performance.tools.aws.Aws;
import com.atlassian.performance.tools.infrastructure.app.AppSource;
import com.atlassian.performance.tools.infrastructure.app.MavenApp;
import com.atlassian.performance.tools.jiraactions.ActionType;
import com.atlassian.performance.tools.jiraactions.ActionTypes;
import com.atlassian.performance.tools.jiraperformancetests.JiraPerformanceTest;
import com.atlassian.performance.tools.jiraperformancetests.Results;
import com.atlassian.performance.tools.report.Criteria;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.io.File;
import java.util.Map;

import static com.amazonaws.regions.Regions.EU_WEST_1;
import static java.time.Duration.ofMinutes;
import static java.util.UUID.randomUUID;

public class SlowEventListenerPerformanceIT {
    private final JiraPerformanceTest jiraPerformanceTest = new JiraPerformanceTest(
            new Aws(
                    EU_WEST_1,
                    new Builder(
                            "arn:aws:iam::695067801333:role/JPT",
                            "api-tests-" + randomUUID()
                    ).build()
            )
    );

    @Test
    public void shouldNotBeSlowerThanBaseline() {
        // given
        final String groupId = "com.atlassian.performance.tools";
        final String artifactId = "reference-jira-app";
        final AppSource experimentApp = new MavenApp(groupId, artifactId, "1.0-SNAPSHOT");
        final AppSource baselineApp = new MavenApp(groupId, artifactId, "1.0-SNAPSHOT");

        final File testFatJar = new File("target/reference-jira-app-performance-tests-1.0-SNAPSHOT-fat-tests.jar");

        final Map<ActionType<?>, Criteria> criteria = ImmutableMap.of(
                ActionTypes.CREATE_ISSUE_SUBMIT,
                new Criteria(
                        0.20f,
                        12,
                        3
                )
        );

        // when
        final Results results = jiraPerformanceTest.runRegressionTest(
                testFatJar,
                MyScenario.class,
                criteria,
                experimentApp,
                baselineApp,
                "7.2.12",
                ofMinutes(10)
        );

        // then
        jiraPerformanceTest.assertNoRegression(results);
    }
}