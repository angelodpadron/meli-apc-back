package org.meliapp.backend.service

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.meliapp.backend.dto.meli.MeliSearchResponse
import org.meliapp.backend.exception.apc.ProductNotFoundException
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers.method
import org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import org.springframework.test.web.client.response.MockRestResponseCreators.withResourceNotFound
import org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess
import org.springframework.web.client.RestClient
import kotlin.test.assertEquals


@ExtendWith(SpringExtension::class)
@RestClientTest(MeliSearchService::class)
class MeliSearchServiceUnitTest {

    @Autowired
    private lateinit var meliSearchService: MeliSearchService

    @Autowired
    private lateinit var server: MockRestServiceServer


    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @TestConfiguration
    class Configuration {
        @Bean
        fun restClient(restClientBuilder: RestClient.Builder) =
            restClientBuilder.build()
    }

    @Test
    fun `find by keyword should return a meli search response`() {
        val keyword = "chipa"
        val categoryId = "category"
        val categoryValue = "MLA123"
        val expectedUri = "sites/${meliSearchService.siteId}/search?q=$keyword&$categoryId=$categoryValue"
        val mockResponse = MeliSearchResponse()

        server.expect(requestTo(expectedUri))
            .andExpect(method(HttpMethod.GET))
            .andRespond(
                withSuccess(
                    "{ \"results\": [], \"available_filters\": [], \"filters\": [] }",
                    MediaType.APPLICATION_JSON
                )
            )

        val response = meliSearchService.findByKeyword(keyword, mapOf(categoryId to categoryValue))

        assertEquals(mockResponse, response)

    }

    @Test
    fun `find by id should return a product response`() {
        val id = "MLA123"
        val expectedUri = "/items/$id"
        val expectedDescriptionUri = "$expectedUri/description"

        server.expect(requestTo(expectedUri))
            .andExpect(method(HttpMethod.GET))
            .andRespond(
                withSuccess(
                    "{ \"id\": \"$id\", \"title\": \"\", \"price\": 0, \"thumbnail\": \"\", \"available_quantity\": 0, \"pictures\": [] }",
                    MediaType.APPLICATION_JSON
                )
            )

        server.expect(requestTo(expectedDescriptionUri))
            .andExpect(method(HttpMethod.GET))
            .andRespond(
                withSuccess(
                    "{ \"plain_text\": \"description\"}",
                    MediaType.APPLICATION_JSON
                )
            )

        val response = meliSearchService.findById(id)

        assertEquals(id, response.meliId)
    }

    @Test
    fun `find by id should throw an exception when the product is not found by id`() {
        val id = "MLA123"
        val expectedUri = "/items/$id"

        server.expect(requestTo(expectedUri))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withResourceNotFound())

        assertThrows<ProductNotFoundException> { meliSearchService.findById(id) }

    }

}