package org.meliapp.backend.service

import org.meliapp.backend.dto.meli.MeliSearchResponse
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

@Service
class MeliSearchService(
    private val restClient: RestClient,
) {

    fun findByKeyword(keyword: String, filters: Map<String, String>?): MeliSearchResponse {

        val queryString = (filters ?: emptyMap())
            .minus("keyword")
            .entries
            .fold("/search?q=$keyword") { acc, entry ->
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



}