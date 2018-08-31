# Example performance test against running Jira instance

The [MyBtf](src/test/java/com/atlassian/performance/tools/btftest/MyJiraOnPermiseIT.java) shows how to consume
`com.atlassian.performance.tools.jiraperformancetests.api.OnPremisePerformanceTest` API to create a test that measures 
running Jira performance

To run the test execute: `./mvnw verify` from your command line