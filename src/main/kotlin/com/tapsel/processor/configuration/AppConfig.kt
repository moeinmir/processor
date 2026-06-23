package com.tapsel.processor.configuration

import com.tapsel.common.dto.EventDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.web.client.RestTemplate
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Configuration
class AppConfig {

    @Bean
    fun restTemplate(): RestTemplate = RestTemplate()

    @Bean
    fun redisTemplate(factory: RedisConnectionFactory): StringRedisTemplate =
        StringRedisTemplate(factory)

    @Bean
    fun kafkaTemplate(factory: ProducerFactory<String, EventDto>): KafkaTemplate<String, EventDto> =
        KafkaTemplate(factory)

    @Bean
    fun executorService(): ExecutorService =
        Executors.newFixedThreadPool(3)
}
