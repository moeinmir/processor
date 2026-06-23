package com.tapsel.processor.service

import com.tapsel.processor.configuration.EventRestConfig
import com.tapsel.processor.rest.RestClient
import org.springframework.stereotype.Service
import com.tapsel.processor.dto.EventsResponseDto
import org.springframework.http.HttpMethod
import com.tapsel.common.constant.EventType

@Service
class WriteEventsStreamOnRedisService(
    private val restClient: RestClient,
    private val eventRestConfig: EventRestConfig,
    private val eventRedisHandlerService: EventRedisHandlerService,
) {
    fun runEventMessageProducingJob() {
        while (true) {
            try {
                val since = eventRedisHandlerService.getLastSince();
                val response = restClient.execute<Any, EventsResponseDto>(
                    eventRestConfig.baseUrl, HttpMethod.GET,emptyMap(),mapOf(
                        eventRestConfig.pathVariable to since),{}, EventsResponseDto::class.java)
                eventRedisHandlerService.setLastSince(response?.startTime.toString())
                if (response?.events?.isEmpty() ?: true) {
                    Thread.sleep(3000)
                    continue
                }
                response.events.forEach { event ->
                    if (event.eventType== EventType.Impression){
                            eventRedisHandlerService.write(event)
                    }
                }
                eventRedisHandlerService.setLastSince(response.endTime.toString())
            } catch (e: Exception) {
                Thread.sleep(3000)
                println(e.stackTrace)
            }
        }
    }
}
