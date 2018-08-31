package com.atlassian.performance.tools.jiraperformancetests.api

import com.atlassian.performance.tools.awsinfrastructure.InfrastructureFormula
import com.atlassian.performance.tools.concurrency.submitWithLogContext
import com.atlassian.performance.tools.infrastructure.api.virtualusers.LoadProfile
import com.atlassian.performance.tools.io.ensureDirectory
import com.atlassian.performance.tools.jiraactions.MergingActionMetricsParser
import com.atlassian.performance.tools.jiraactions.scenario.Scenario
import com.atlassian.performance.tools.report.api.parser.MergingNodeCountParser
import com.atlassian.performance.tools.report.api.parser.SystemMetricsParser
import com.atlassian.performance.tools.report.api.result.CohortResult
import com.atlassian.performance.tools.report.api.result.FailedCohortResult
import com.atlassian.performance.tools.report.api.result.FullCohortResult
import com.atlassian.performance.tools.workspace.api.TestWorkspace
import org.apache.logging.log4j.CloseableThreadContext
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.nio.file.Path
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit

class ProvisioningPerformanceTest(
    private val infrastructureFormula: InfrastructureFormula<*>,
    private val cohort: String
) {

    private val logger: Logger = LogManager.getLogger(this::class.java)

    fun runAsync(
        workingDirectory: TestWorkspace,
        executor: ExecutorService,
        loadProfile: LoadProfile,
        scenarioClass: Class<out Scenario>? = null,
        diagnosticsLimit: Int? = null
    ): CompletableFuture<CohortResult> {
        return executor.submitWithLogContext(cohort) {
            CloseableThreadContext.put("cohort", cohort).use {
                run(workingDirectory, loadProfile, scenarioClass, diagnosticsLimit)
            }
        }
    }

    fun run(
        workingDirectory: TestWorkspace,
        loadProfile: LoadProfile,
        scenarioClass: Class<out Scenario>? = null,
        diagnosticsLimit: Int? = null
        ): CohortResult {
        val workspace = workingDirectory.directory.resolve(cohort).ensureDirectory()
        try {
            val provisionedInfrastructure = infrastructureFormula.provision(workspace)
            val infrastructure = provisionedInfrastructure.infrastructure
            val resource = provisionedInfrastructure.resource
            val downloadedResults: Path
            try {
                infrastructure.applyLoad(loadProfile, scenarioClass, diagnosticsLimit)
            } catch (e: Exception) {
                logger.error("Failed to test on $infrastructure", e)
                throw e
            } finally {
                if (resource.isExpired()) {
                    logger.warn("$resource is already expired, but the test just finished")
                }
                downloadedResults = infrastructure.downloadResults(workspace)
            }
            logger.info("Freeing AWS resources...")
            resource.release().get(2, TimeUnit.MINUTES)
            logger.info("AWS resources are freed")
            return FullCohortResult(
                cohort = cohort,
                results = downloadedResults,
                actionParser = MergingActionMetricsParser(),
                systemParser = SystemMetricsParser(),
                nodeParser = MergingNodeCountParser()
            )
        } catch (e: Exception) {
            return FailedCohortResult(cohort, e)
        }
    }
}