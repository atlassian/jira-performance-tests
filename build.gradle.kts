import com.vanniktech.dependency.graph.generator.DependencyGraphGeneratorExtension
import org.apache.tools.ant.taskdefs.condition.Os.*
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import java.nio.file.Paths
import com.vanniktech.dependency.graph.generator.DependencyGraphGeneratorPlugin
import com.vanniktech.dependency.graph.generator.DependencyGraphGeneratorExtension.Generator
import guru.nidi.graphviz.attribute.Color
import guru.nidi.graphviz.attribute.Style

val kotlinVersion = "1.2.30"

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("com.vanniktech:gradle-dependency-graph-generator-plugin:0.5.0")
    }
}

plugins {
    kotlin("jvm").version("1.2.30")
    id("com.atlassian.performance.tools.gradle-release").version("0.4.3")
    `java-library`
}

plugins.apply("com.vanniktech.dependency.graph.generator")

configurations.all {
    resolutionStrategy {
        activateDependencyLocking()
        failOnVersionConflict()
        eachDependency {
            when (requested.module.toString()) {
                "com.google.guava:guava" -> useVersion("23.6-jre")
                "org.apache.commons:commons-csv" -> useVersion("1.4")
                "commons-logging:commons-logging" -> useVersion("1.2")
                "org.apache.httpcomponents:httpclient" -> useVersion("4.5.5")
                "org.apache.httpcomponents:httpcore" -> useVersion("4.4.9")
                "org.codehaus.plexus:plexus-utils" -> useVersion("3.1.0")
                "org.slf4j:slf4j-api" -> useVersion("1.8.0-alpha2")
                "com.jcraft:jzlib" -> useVersion("1.1.3")
                "com.google.code.gson:gson" -> useVersion("2.8.2")
                "org.jsoup:jsoup" -> useVersion("1.10.2")
                "com.fasterxml.jackson.core:jackson-core" -> useVersion("2.9.4")
            }
        }
    }
}

val jptDependenciesGenerator = Generator(
    "jptLibraries",
    { dependency -> dependency.moduleGroup.startsWith("com.atlassian.performance.tools") },
    { _ -> true },
    { node, _ -> node.add(Style.FILLED, Color.rgb("#ffcb2b")) }
)

configure<DependencyGraphGeneratorExtension> {
    generators = listOf(jptDependenciesGenerator)
}

dependencies {

    api("com.atlassian.performance.tools:workspace:[2.0.0,3.0.0)")
    api("com.atlassian.performance.tools:report:[2.1.0,3.0.0)")
    api("com.atlassian.performance.tools:infrastructure:[2.1.0,3.0.0)")

    listOf(
        "org.jetbrains.kotlin:kotlin-stdlib-jre8:$kotlinVersion",
        "com.atlassian.performance.tools:aws-resources:[1.0.0,2.0.0)",
        "com.atlassian.performance.tools:aws-infrastructure:[1.0.0,2.0.0)",
        "com.atlassian.performance.tools:jira-software-actions:[1.0.0,2.0.0)",
        "com.atlassian.performance.tools:virtual-users:[2.2.0,3.0.0)"
    ).plus(
        log4jCore()
    ).forEach { implementation(it) }

    listOf(
        "junit:junit:4.12",
        "org.assertj:assertj-core:3.11.0"
    ).forEach { testCompile(it) }
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

tasks.getByName("test", Test::class).apply {
    useJUnit {
        excludeCategories("com.atlassian.performance.tools.jiraperformancetests.AcceptanceCategory")
    }
}

val testAcceptance = task<Test>("testAcceptance") {
    dependsOn("publishToMavenLocal")
    systemProperty("jpt.version", version)
    maxParallelForks = 2
}

tasks.withType(Test::class.java) {
    val shadowJarTask = tasks.getByPath(":reference-virtual-users:shadowJar")
    dependsOn(shadowJarTask)
    systemProperty("jpt.virtual-users.shadow-jar", shadowJarTask.outputs.files.files.first())
}

tasks["release"].dependsOn(testAcceptance)

task<Wrapper>("wrapper") {
    gradleVersion = "4.9"
    distributionType = Wrapper.DistributionType.ALL
}