package org.meliapp.backend.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.meliapp.backend.dto.product.ProductResponse
import org.meliapp.backend.exception.apc.ProductNotFoundException
import org.meliapp.backend.model.Product
import org.meliapp.backend.repository.ProductRepository
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.kotlin.*
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.math.BigDecimal
import java.util.*

@ExtendWith(SpringExtension::class)
class ProductServiceUnitTest {

    @Mock
    private lateinit var meliSearchService: MeliSearchService

    @Mock
    private lateinit var productRepository: ProductRepository

    @InjectMocks
    private lateinit var productService: ProductService

    @Test
    fun `can find a product using it's meli id locally`() {
        // Arrange
        val product: Product = mock()
        val meliId = "MELI_ID"

        whenever(product.meliId).thenReturn(meliId)
        whenever(productRepository.findByMeliId(meliId)).thenReturn(Optional.of(product))

        // Act
        val result = productService.findByMeliIdOld(meliId)

        // Assert
        assertTrue(result.isPresent)
        assertEquals(meliId, result.get().meliId)

    }

    @Test
    fun `should use search service when cannot find product locally`() {
        val meliId = "MELI_ID"

        val product = Product().apply {
            title = "title"
            thumbnail = "thumbnail"
            price = BigDecimal(999)
            this.meliId = meliId
        }

        val productResponse = ProductResponse(
            id = meliId,
            thumbnail = product.thumbnail,
            price = product.price,
            title = product.title,
            availableQuantity = 1
        )

        whenever(productRepository.findByMeliId(any())).thenReturn(Optional.empty())
        whenever(meliSearchService.findById(any())).thenReturn(productResponse)
        whenever(productRepository.save(any<Product>())).thenReturn(product)

        val result = productService.findByMeliId(meliId)

        assertEquals(product.id, result.id)
        verify(productRepository, times(1)).save(any<Product>())
    }

    @Test
    fun `should throw an exception when the cannot find the product with the given id`() {
        val meliId = "MELI_ID"

        whenever(productRepository.findByMeliId(meliId)).thenReturn(Optional.empty())
        whenever(meliSearchService.findById(meliId)).thenThrow(ProductNotFoundException::class.java)

        assertThrows<ProductNotFoundException> { productService.findByMeliId(meliId) }
    }


}