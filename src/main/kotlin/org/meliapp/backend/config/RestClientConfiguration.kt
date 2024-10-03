package org.meliapp.backend.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient

@Configuration
class RestClientConfiguration {

    @Value("\${app.meli.base-url}")
    private lateinit var baseUrl: String

    @Value("\${app.meli.access-token}")
    private lateinit var accessToken: String

    @Bean
    fun restClient(): RestClient {
        return RestClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader("Authorization", "Bearer $accessToken")
            .build()
    }

}