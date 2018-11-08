package com.atlassian.performance.tools.jiraperformancetests

class SystemProperty(
    private val name: String
) {

    fun dereference(): String = System.getProperty(name) ?: throw Exception("`$name` should be set")
}