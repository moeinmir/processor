package com.tapsel.processor.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.tapsel.common.dto.EventDto
import com.tapsel.common.util.TimeBucketHelper.Companion.bucketFromEpochMillis
import com.tapsel.processor.configuration.RedisKeyConfig
import com.tapsel.processor.redis.RedisClient
import org.springframework.stereotype.Component

@Component
class EventRedisHandlerService(
    private val redisClient: RedisClient,
    private val redisKeyConfig: RedisKeyConfig,
    private val objectMapper: ObjectMapper
) {

    fun write(event: EventDto) {
        val impressionId = event.impressionId ?: return
        val bucket = bucketFromEpochMillis(event.eventTime)
        val key = buildKey(bucket, impressionId)
        val value = objectMapper.writeValueAsString(event)
        redisClient.set(key, value)
    }

    fun read(bucket: String, impressionId: String): EventDto? {
        val key = buildKey(bucket, impressionId)
        val value = redisClient.get(key) ?: return null
        return try {
            objectMapper.readValue(value, EventDto::class.java)
        } catch (ex: Exception) {
            null
        }
    }

    fun readRaw(key: String): EventDto? {
        val value = redisClient.get(key) ?: return null
        return objectMapper.readValue(value, EventDto::class.java)
    }

    fun getLastSince(): String =
        redisClient.get(redisKeyConfig.lastEventTime) ?: "1"

    fun setLastSince(lastTime: String) {
        redisClient.set(redisKeyConfig.lastEventTime, lastTime)
    }

    private fun buildKey(bucket: String, impressionId: String): String =
        "impression:$bucket:$impressionId"
}
