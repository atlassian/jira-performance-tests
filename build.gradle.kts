val kotlinVersion = "1.2.30"

plugins {
    kotlin("jvm").version("1.2.30")
    id("com.atlassian.performance.tools.gradle-release").version("0.0.2")
}

dependencies {
    listOf(
        "org.jetbrains.kotlin:kotlin-stdlib-jre8:$kotlinVersion",
        "com.atlassian.performance.tools:workspace:0.0.1",
        "com.atlassian.performance.tools:report:0.0.1",
        "com.atlassian.performance.tools:aws-resources:0.0.1",
        "com.atlassian.performance.tools:infrastructure:0.1.0",
        "com.atlassian.performance.tools:aws-infrastructure:0.0.1",
        "com.atlassian.performance.tools:jira-software-actions:0.1.1",
        "com.atlassian.performance.tools:virtual-users:0.0.3"
    ).plus(
        log4jCore()
    ).forEach { compile(it) }
}

fun log4jCore(): List<String> = log4j(
    "api",
    "core",
    "slf4j-impl"
)

fun log4j(
    vararg modules: String
): List<String> = modules.map { module ->
    "org.apache.logging.log4j:log4j-$module:2.10.0"
}

task<Wrapper>("wrapper") {
    gradleVersion = "4.9"
    distributionType = Wrapper.DistributionType.ALL
}