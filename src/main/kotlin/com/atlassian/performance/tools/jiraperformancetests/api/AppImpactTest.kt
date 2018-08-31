package com.atlassian.performance.tools.jiraperformancetests.api

import com.atlassian.performance.tools.aws.Aws
import com.atlassian.performance.tools.infrastructure.api.app.MavenApp
import com.atlassian.performance.tools.infrastructure.api.app.NoApp
import com.atlassian.performance.tools.jiraactions.ActionType
import com.atlassian.performance.tools.jiraactions.scenario.Scenario
import com.atlassian.performance.tools.jirasoftwareactions.JiraSoftwareScenario
import com.atlassian.performance.tools.report.api.Criteria
import java.io.File
import java.time.Duration

/**
 * Tests the performance impact of the [app].
 */
class AppImpactTest(
    private val app: MavenApp,
    private val aws: Aws
) {
    var testJar: File = File("target/${app.artifactId}-performance-tests-${app.version}-fat-tests.jar")
    var scenario: Class<out Scenario> = JiraSoftwareScenario::class.java
    var criteria: Map<ActionType<*>, Criteria> = emptyMap()
    var jiraVersion: String = "7.5.0"
    var duration: Duration = Duration.ofMinutes(20)

    fun run() {
        val test = AppRegressionTest(
            aws = aws,
            duration = duration
        )
        val results = test.run(
            testJar = testJar,
            scenario = scenario,
            baselineApp = NoApp(),
            experimentApp = app,
            jiraVersion = jiraVersion
        )
        test.assertNoRegression(results, criteria)
    }
}