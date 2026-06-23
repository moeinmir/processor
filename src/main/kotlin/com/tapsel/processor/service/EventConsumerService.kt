package com.tapsel.processor.service

import com.tapsel.common.constant.EventType
import com.tapsel.common.dto.EventDto
import com.tapsel.processor.redis.RedisClient
import org.springframework.stereotype.Component

import com.tapsel.common.util.TimeBucketHelper.Companion.bucketFromEpochMillis
import org.springframework.kafka.annotation.KafkaListener

@Component
class EventConsumerService(
    private val redisClient: RedisClient,
    private val eventRedisHandlerService: EventRedisHandlerService,
    private val adCrudOperationService : AdCrudOperationService

) {

    @KafkaListener(topics = ["click-topic"])
    fun consume(event: EventDto) {
        val bucket = bucketFromEpochMillis(event.eventTime)
        val pair = eventRedisHandlerService.read(bucket,event.impressionId!!)
        val contract = adCrudOperationService.getContractsByEventDto(pair!!)?.payOrGetPayed(EventType.Click)
        adCrudOperationService.saveContract(contract!!)
    }
}

