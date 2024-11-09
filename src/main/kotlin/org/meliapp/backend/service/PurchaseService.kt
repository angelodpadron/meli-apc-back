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

    fun buy(purchaseRequest: PurchaseRequest): PurchaseResponse =
        toPurchaseResponse(
            purchaseRepository.save(
                Purchase().apply {
                    this.user = authService.getUserAuthenticated()
                    this.product = productService.findByMeliId(purchaseRequest.meliId)
                    quantity = purchaseRequest.quantity
                    totalPrice = product.price * BigDecimal(quantity)
                }
            )
        )

    fun purchases(): List<PurchaseResponse> =
        purchaseRepository
            .findByUserId(authService.getUserAuthenticated().id)
            .map { toPurchaseResponse(it) }

    private fun toPurchaseResponse(purchase: Purchase): PurchaseResponse =
        PurchaseResponse(
            purchase.id,
            purchase.quantity,
            toProductPurchaseResponse(purchase.product),
            purchase.purchaseDate,
            purchase.totalPrice
        )

    private fun toProductPurchaseResponse(product: Product): ProductPurchaseResponse =
        ProductPurchaseResponse(
            product.meliId,
            product.title,
            product.thumbnail,
            product.price
        )


}