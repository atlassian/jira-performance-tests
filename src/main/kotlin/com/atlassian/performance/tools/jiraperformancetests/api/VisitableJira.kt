package com.atlassian.performance.tools.jiraperformancetests.api

import java.net.URI
import javax.json.Json
import javax.json.JsonObject

class VisitableJira(
    val address: URI
) {
    constructor(
        json: JsonObject
    ) : this(
        address = URI(json.getString("address"))
    )

    fun toJson() = Json.createObjectBuilder()
        .add("address", address.toString())
        .build()
        .toString()
}