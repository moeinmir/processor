package com.tapsel.processor.rest

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@Component
class RestClient(private val restTemplate: RestTemplate) {
    fun <REQ, RES> execute(
        url: String,
        method: HttpMethod,
        pathVariables: Map<String, String>,
        queryParameters: Map<String, String>,
        body: REQ?,
        responseType: Class<RES>
    ): RES? {
        val builder = UriComponentsBuilder.fromUriString(url)
        queryParameters.forEach { (key, value) -> builder.queryParam(key, value) }
        val finalUri: URI = builder.buildAndExpand(pathVariables).toUri()
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val requestEntity = HttpEntity(body, headers)
        return try {
            restTemplate.exchange(finalUri, method, requestEntity, responseType).body
        } catch (e: Exception) {
            throw RuntimeException("rest.call.error")
        }
    }
}
