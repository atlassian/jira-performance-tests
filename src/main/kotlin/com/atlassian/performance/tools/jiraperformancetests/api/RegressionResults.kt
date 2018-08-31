package com.atlassian.performance.tools.jiraperformancetests.api

import com.atlassian.performance.tools.report.api.result.CohortResult

data class RegressionResults(
    val baseline: CohortResult,
    val experiment: CohortResult
)