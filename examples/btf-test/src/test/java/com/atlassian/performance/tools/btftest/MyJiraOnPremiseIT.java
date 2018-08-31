package com.atlassian.performance.tools.btftest;

import com.atlassian.performance.tools.jiraperformancetests.api.OnPremisePerformanceTest;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;

public class MyJiraOnPremiseIT {

    /**
     * Defaults to yield results quickly. Needs additional configuration to achieve greater meaningfulness.
     */
    @Test
    public void testMyJira() throws URISyntaxException {

        /*
         * Point this toward tested Jira.
         */
        final URI myJira = new URI("http://localhost:8090/jira/");

        final OnPremisePerformanceTest btfTest = new OnPremisePerformanceTest(myJira);

        /*
         * Set credentials so the test knows how to access your jira.
         */
        btfTest.setAdminLogin("admin");
        btfTest.setAdminPassword("admin");

        /*
         * Optionally, set the number of virtual users that will generate the load.
         */
        btfTest.setVirtualUsers(1);

        /*
         * Optionally, change the test duration.
         */
        btfTest.setTestDuration(Duration.ofMinutes(5));

        btfTest.run();
    }
}
