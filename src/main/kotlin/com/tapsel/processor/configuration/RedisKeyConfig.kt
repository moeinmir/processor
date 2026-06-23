package com.tapsel.processor.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.redis.keys")
data class RedisKeyConfig(
    val lastEventTime: String,
)
