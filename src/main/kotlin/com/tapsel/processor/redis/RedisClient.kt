package com.tapsel.processor.redis

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component

@Component
class RedisClient(
    private val redisTemplate: StringRedisTemplate
) {

    fun get(key: String): String? = redisTemplate.opsForValue().get(key)

    fun set(key: String, value: String) =
        redisTemplate.opsForValue().set(key, value)

    fun delete(key: String): Boolean =
        redisTemplate.delete(key)

    fun setWithTtl(key: String, value: String, ttlSeconds: Long) {
        redisTemplate.opsForValue().set(key, value, ttlSeconds, java.util.concurrent.TimeUnit.SECONDS)
    }

}
