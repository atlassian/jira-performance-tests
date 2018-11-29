package com.atlassian.performance.tools.jiraperformancetests.api

import com.atlassian.performance.tools.report.api.result.CohortResult

class RegressionResults(
    val baseline: CohortResult,
    val experiment: CohortResult
)