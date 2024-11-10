package org.meliapp.backend.integration

import jakarta.transaction.Transactional
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.meliapp.backend.exception.apc.ProductNotFoundException
import org.meliapp.backend.integration.base.BaseIntegrationTest
import org.meliapp.backend.model.Bookmark
import org.meliapp.backend.model.Product
import org.meliapp.backend.repository.BookmarkRepository
import org.meliapp.backend.repository.ProductRepository
import org.meliapp.backend.service.MeliSearchService
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BookmarkIntegrationTest(
    @Autowired val bookmarkRepository: BookmarkRepository,
    @Autowired val productRepository: ProductRepository,
) : BaseIntegrationTest() {

    @MockBean
    private lateinit var meliSearchService: MeliSearchService

    lateinit var persistedBookmark: Bookmark

    @BeforeEach
    fun setup() {
        productRepository.deleteAll()
        bookmarkRepository.deleteAll()

        productRepository.save(Product().apply {
            meliId = "meli_id"
            title = "product_title"
            price = BigDecimal(1)
            thumbnail = "thumbnail"
        })

        persistedBookmark = bookmarkRepository.save(Bookmark().apply {
            stars = 1
            comment = "comment"
            product = productRepository.findByMeliId("meli_id").get()
            user = userRepository.findByEmail("test@test.com").get()
        })

    }

    @Test
    fun `should return list of bookmark and a 200 code status`() {
        makeAuthGetRequest("/api/bookmarks").andExpect(status().isOk)
    }

    @Test
    fun `should create a bookmark and a 201 code status`() {
        productRepository.save(Product().apply {
            meliId = "new_meli_id"
            title = "product_title"
            price = BigDecimal(1)
            thumbnail = "thumbnail"
        })

        makeAuthPostRequest(
            "/api/bookmarks", makeBookmarkContent("new_meli_id", "comment", 5)
        ).andExpect(status().isCreated)

    }

    @Test
    fun `should not create a bookmark for an non existing product and return a 400 code status`() {

        whenever(meliSearchService.findById(any<String>())).thenThrow(ProductNotFoundException::class.java)

        makeAuthPostRequest(
            "/api/bookmarks", makeBookmarkContent("bad_id", "comment", 5)
        ).andExpect(status().isNotFound)
    }

    @Test
    fun `should return the details of a bookmark and a 200 code status`() {
        makeAuthGetRequest("/api/bookmarks/${persistedBookmark.id}").andExpect(status().isOk)
    }

    @Test
    fun `should update a bookmark`() {
        makeAuthPutRequest(
            "/api/bookmarks/${persistedBookmark.id}", makeBookmarkContent("meli_id", "new_comment", 2)
        ).andExpect(status().isOk)
    }

    @Test
    fun `should not update a non existing bookmark and return a 404 status code`() {

        makeAuthPutRequest(
            "/api/bookmarks/${persistedBookmark.id + 1}", makeBookmarkContent("meli_id", "comment", 2)
        ).andExpect(status().isNotFound)
    }

    @Test
    fun `should delete a bookmark and return a 200 code status`() {
        mockMvc.perform(
            delete("/api/bookmarks/${persistedBookmark.id}").header(HttpHeaders.AUTHORIZATION, "Bearer $authToken")
        ).andExpect(status().isOk)
    }

    @Test
    fun `should not delete a non existing bookmark and return a 404 code status`() {
        makeAuthDeleteRequest("/api/bookmarks/${persistedBookmark.id + 1}").andExpect(status().isNotFound)
    }

    private fun makeBookmarkContent(meliId: String, comment: String, stars: Int) =
        """{ "meli_id": "$meliId","stars": "$stars", "comment": "$comment" }"""


}