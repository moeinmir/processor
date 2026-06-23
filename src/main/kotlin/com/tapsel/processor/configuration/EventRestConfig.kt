package com.tapsel.processor.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.rest-client.events")
data class EventRestConfig(
    val baseUrl: String,
    val pathVariable: String,
)
