package com.atlassian.performance.tools.referencejiraapp.scenario;

import com.atlassian.performance.tools.jiraactions.SeededRandom;
import com.atlassian.performance.tools.jiraactions.WebJira;
import com.atlassian.performance.tools.jiraactions.action.Action;
import com.atlassian.performance.tools.jiraactions.action.BrowseProjectsAction;
import com.atlassian.performance.tools.jiraactions.action.CreateIssueAction;
import com.atlassian.performance.tools.jiraactions.action.SearchJqlAction;
import com.atlassian.performance.tools.jiraactions.measure.ActionMeter;
import com.atlassian.performance.tools.jiraactions.memories.IssueKeyMemory;
import com.atlassian.performance.tools.jiraactions.memories.adaptive.AdaptiveIssueKeyMemory;
import com.atlassian.performance.tools.jiraactions.memories.adaptive.AdaptiveJqlMemory;
import com.atlassian.performance.tools.jiraactions.memories.adaptive.AdaptiveProjectMemory;
import com.atlassian.performance.tools.jiraactions.scenario.Scenario;
import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MyScenario implements Scenario {
    @NotNull
    @Override
    public List<Action> getActions(WebJira webJira, SeededRandom seededRandom, ActionMeter actionMeter) {

        final AdaptiveJqlMemory jqlMemory = new AdaptiveJqlMemory(seededRandom);
        final IssueKeyMemory issueKeyMemory = new AdaptiveIssueKeyMemory(seededRandom);
        final AdaptiveProjectMemory adaptiveProjectMemory = new AdaptiveProjectMemory(seededRandom);
        return ImmutableList.of(
                new SearchJqlAction(webJira, actionMeter, jqlMemory, issueKeyMemory),
                new BrowseProjectsAction(webJira, actionMeter, adaptiveProjectMemory),
                new CreateIssueAction(webJira, actionMeter, adaptiveProjectMemory, seededRandom),
                new CreateIssueAction(webJira, actionMeter, adaptiveProjectMemory, seededRandom),
                new CreateIssueAction(webJira, actionMeter, adaptiveProjectMemory, seededRandom),
                new CreateIssueAction(webJira, actionMeter, adaptiveProjectMemory, seededRandom),
                new CreateIssueAction(webJira, actionMeter, adaptiveProjectMemory, seededRandom),
                new CreateIssueAction(webJira, actionMeter, adaptiveProjectMemory, seededRandom),
                new CreateIssueAction(webJira, actionMeter, adaptiveProjectMemory, seededRandom),
                new CreateIssueAction(webJira, actionMeter, adaptiveProjectMemory, seededRandom),
                new CustomViewIssueAction(webJira, actionMeter, issueKeyMemory)
        );
    }
}
