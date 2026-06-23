//package com.tapsel.processor.service
//
//@Component
//class EventConsumerService(
//    private val redisClient: RedisClient,
//    private val bucketHelper: TimeBucketHelper
//) {
//
//    @KafkaListener(topics = ["impression-topic"])
//    fun consume(event: EventDto) {
//
//        val bucket = bucketHelper.bucketFromEpochMillis(event.eventTime)
//
//        val key = "impression:$bucket:${event.impressionId}"
//
//        redisClient.setWithTtl(
//            key,
//            event.toCompactString(),
//            15 * 60 // 15 min TTL
//        )
//    }
//}
//
