package org.meliapp.backend.service

import org.meliapp.backend.dto.meli.MeliSearchResponse
import org.meliapp.backend.dto.product.ProductResponse
import org.meliapp.backend.exception.apc.ProductNotFoundException
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

@Service
class MeliSearchService(
    private val restClient: RestClient,
) {

    @Value("\${app.meli.meli-site-id}")
    lateinit var siteId: String

    fun findByKeyword(keyword: String, filters: Map<String, String>?): MeliSearchResponse {

        val queryString = (filters ?: emptyMap())
            .minus("keyword")
            .entries
            .fold("sites/$siteId/search?q=$keyword") { acc, entry ->
                "$acc&${entry.key}=${entry.value}"
            }

        return restClient
            .get()
            .uri(queryString)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body(object : ParameterizedTypeReference<MeliSearchResponse>() {})
            ?: MeliSearchResponse()

    }

    fun findById(id: String): ProductResponse {
        val queryString = "/items/${id}"

        return restClient
            .get()
            .uri(queryString)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus({ it.is4xxClientError }) { _, _ -> throw ProductNotFoundException(id) }
            .body(object : ParameterizedTypeReference<ProductResponse>() {})!!

    }


}