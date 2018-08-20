package com.atlassian.performance.tools.referencejiraapp.scenario;

import com.atlassian.performance.tools.jiraactions.ActionTypes;
import com.atlassian.performance.tools.jiraactions.WebJira;
import com.atlassian.performance.tools.jiraactions.action.Action;
import com.atlassian.performance.tools.jiraactions.measure.ActionMeter;
import com.atlassian.performance.tools.jiraactions.memories.IssueKeyMemory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class CustomViewIssueAction implements Action {
    private final static Logger logger = LogManager.getLogger(CustomViewIssueAction.class);

    private final WebJira jira;
    private final ActionMeter meter;
    private final IssueKeyMemory issueKeyMemory;


    CustomViewIssueAction(WebJira jira, ActionMeter meter, IssueKeyMemory issueKeyMemory) {
        this.jira = jira;
        this.meter = meter;
        this.issueKeyMemory = issueKeyMemory;
    }

    @Override
    public void run() {
        final String issueKey = issueKeyMemory.recall();
        if (issueKey == null) {
            logger.info("Skipping View Issue action. I have no knowledge of issue keys.");
            return;
        }
        meter.measure(
                ActionTypes.VIEW_ISSUE,
                () -> jira.goToIssue(issueKey).waitForSummary()
        );
    }
}
