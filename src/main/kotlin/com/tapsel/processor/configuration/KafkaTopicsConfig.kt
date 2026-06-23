package com.tapsel.processor.configuration


import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix="app.kafka.topics")
data class KafkaTopicsConfig (
    val impressionTopic : String,
    val clickTopic : String)
