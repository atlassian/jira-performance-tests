package com.atlassian.performance.tools.jiraperformancetests.api

import com.atlassian.performance.tools.aws.api.Aws
import com.atlassian.performance.tools.aws.api.ProvisionedStack
import com.atlassian.performance.tools.aws.api.currentUser
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.time.Duration
import java.time.Instant.now

/**
 * Cleans up unnecessary AWS resources
 */
class AwsHousekeeping(
    private val aws: Aws
) {
    private val logger: Logger = LogManager.getLogger(this::class.java)

    /**
     * Lists all stacks that were provisioned by current user
     */
    fun listMyStacks() {
        val stacks = aws.listDisposableStacks()
            .map { ProvisionedStack(it, aws) }
            .filter { it.user == currentUser() }
        logger.info("Below is the list of AWS stacks that were provisioned by the current user:")
        if (stacks.isNotEmpty()) {
            stacks.forEach {
                val remainingTime = Duration.between(now(), it.expiry)
                val listedStack = ListedStack(
                    name = it.stackName,
                    user = it.user,
                    timeLeft = if (remainingTime.isNegative) "EXPIRED" else remainingTime.toString(),
                    status = it.status
                )
                logger.info("${it.user}: $listedStack")
            }
        } else {
            logger.info("No stacks found")
        }
    }

    private data class ListedStack(
        val name: String,
        val user: String?,
        val timeLeft: String,
        val status: String
    )

    /**
     * Deletes all stacks (live or expired) that were provisioned by current user
     */
    fun deleteMyStacks() {
        logger.info("Retrieving stacks for the current user...")
        val stacks = aws.listDisposableStacks()
            .map { ProvisionedStack(it, aws) }
            .filter { it.user == currentUser() }
        if (stacks.isNotEmpty()) {
            logger.info("Deleting stacks...")
            stacks
                .map { stack ->
                    stack
                        .release()
                        .thenAccept { logger.info("Deleted ${stack.stackName} ") }
                }
                .forEach { it.get() }
            logger.info("All stacks of the current user have been deleted")
        } else {
            logger.info("No stacks to delete")
        }
    }

    /**
     * Deletes every stack and every instance that exceeded its lifespan for all users
     */
    fun cleanAllExpired() {
        logger.info("Cleaning expired resources...")
        aws.cleanLeftovers()
        logger.info("All expired resources have been cleaned")
    }

}