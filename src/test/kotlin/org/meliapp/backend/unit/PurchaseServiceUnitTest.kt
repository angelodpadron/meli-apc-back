package org.meliapp.backend.unit

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.meliapp.backend.dto.purchase.PurchaseRequest
import org.meliapp.backend.model.Product
import org.meliapp.backend.model.Purchase
import org.meliapp.backend.model.User
import org.meliapp.backend.repository.PurchaseRepository
import org.meliapp.backend.service.AuthService
import org.meliapp.backend.service.ProductService
import org.meliapp.backend.service.PurchaseService
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.kotlin.*
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.math.BigDecimal
import kotlin.test.assertEquals

@ExtendWith(SpringExtension::class)
class PurchaseServiceUnitTest {

    @Mock
    lateinit var purchaseRepository: PurchaseRepository

    @Mock
    lateinit var productService: ProductService

    @Mock
    lateinit var authService: AuthService

    @InjectMocks
    lateinit var purchaseService: PurchaseService

    @BeforeEach
    fun setup() {
        val user: User = mock()
        whenever(user.id).thenReturn(1)
        whenever(authService.getUserAuthenticated()).thenReturn(user)
    }

    @AfterEach
    fun cleanup() {
        reset(purchaseRepository, productService, authService)
    }

    @Test
    fun `can buy an existing product`() {
        val meliId = "MELI_ID"
        val productTitle = "TITLE"
        val thumbnail = "THUMBNAIL"
        val product: Product = mock()

        whenever(product.meliId).thenReturn(meliId)
        whenever(product.price).thenReturn(BigDecimal.ZERO)
        whenever(product.title).thenReturn(productTitle)
        whenever(product.thumbnail).thenReturn(thumbnail)
        whenever(productService.findByMeliId(meliId)).thenReturn(product)
        whenever(purchaseRepository.save(any<Purchase>())).thenAnswer { it.getArgument<Purchase>(0) }

        val result = purchaseService.buy(PurchaseRequest(meliId, 1))

        assertEquals(meliId, result.product.meliId)
        assertEquals(productTitle, result.product.title)
        assertEquals(thumbnail, result.product.thumbnail)
        assertEquals(BigDecimal.ZERO, result.product.price)
        assertEquals(1, result.quantity)
        assertEquals(BigDecimal.ZERO, result.total)

        verify(purchaseRepository, times(1)).save(any<Purchase>())

    }

    @Test
    fun `can get all the purchases from current user`() {
        whenever(purchaseRepository.findByUserId(any())).thenReturn(listOf())

        val result = purchaseService.purchases()

        assertEquals(0, result.size)
    }

}