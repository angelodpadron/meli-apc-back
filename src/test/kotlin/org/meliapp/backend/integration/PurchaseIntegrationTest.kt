package org.meliapp.backend.integration

import jakarta.transaction.Transactional
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.meliapp.backend.exception.apc.ProductNotFoundException
import org.meliapp.backend.integration.base.BaseIntegrationTest
import org.meliapp.backend.model.Product
import org.meliapp.backend.repository.ProductRepository
import org.meliapp.backend.repository.PurchaseRepository
import org.meliapp.backend.service.MeliSearchService
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal

@SpringBootTest
@Transactional
class PurchaseIntegrationTest(
    @Autowired private val purchaseRepository: PurchaseRepository,
    @Autowired private val productRepository: ProductRepository
) : BaseIntegrationTest() {

    @MockBean
    private lateinit var meliSearchService: MeliSearchService

    @BeforeEach
    fun setup() {
        productRepository.deleteAll()
        purchaseRepository.deleteAll()

        // Add a product to db
        productRepository.save(Product().apply {
            meliId = "meli_id"
            title = "product_title"
            price = BigDecimal.ZERO
            thumbnail = "product_thumbnail"
        })

    }

    @Test
    fun `should purchase a product and return a 201 status code`() {
        makeAuthPostRequest("/api/purchases", makePurchaseRequestContent("meli_id", 1))
            .andExpect(status().isCreated)
    }

    @Test
    fun `should not purchase a non existing product and return a 404 status code`() {
        productRepository.deleteAll()
        whenever(meliSearchService.findById(any<String>())).thenThrow(ProductNotFoundException::class.java)

        makeAuthPostRequest("/api/purchases", makePurchaseRequestContent("meli_id", 1))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `should get all completed purchases and return a 200 status code`() {
        makeAuthGetRequest("/api/purchases").andExpect(status().isOk)
    }

    private fun makePurchaseRequestContent(meliId: String, quantity: Int) =
        """{ "meli_id": "$meliId","quantity": "$quantity" }"""

}