package org.meliapp.backend.service

import org.meliapp.backend.dto.product.ProductPurchaseResponse
import org.meliapp.backend.dto.purchase.PurchaseRequest
import org.meliapp.backend.dto.purchase.PurchaseResponse
import org.meliapp.backend.model.Product
import org.meliapp.backend.model.Purchase
import org.meliapp.backend.repository.PurchaseRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class PurchaseService(
    private val purchaseRepository: PurchaseRepository,
    private val authService: AuthService,
    private val productService: ProductService
) {

    fun buy(purchaseRequest: PurchaseRequest): PurchaseResponse {
        val user = authService.getUserAuthenticated()
        val product = productService.findByMeliId(purchaseRequest.meliId)

        val purchase = Purchase()
        purchase.user = user
        purchase.quantity = purchaseRequest.quantity
        purchase.product = product
        purchase.totalPrice = product.price * BigDecimal(purchaseRequest.quantity)

        purchaseRepository.save(purchase)

        return toPurchaseResponse(purchase)

    }

    fun purchases(): List<PurchaseResponse> {
        val user = authService.getUserAuthenticated()
        return purchaseRepository.findByUserId(user.id).map { toPurchaseResponse(it) }
    }

    private fun toPurchaseResponse(purchase: Purchase): PurchaseResponse {
        return PurchaseResponse(
            purchase.id,
            purchase.quantity,
            toProductPurchaseResponse(purchase.product),
            purchase.purchaseDate,
            purchase.totalPrice
        )
    }

    private fun toProductPurchaseResponse(product: Product): ProductPurchaseResponse {
        return ProductPurchaseResponse(
            product.meliId,
            product.title,
            product.thumbnail,
            product.price)
    }


}