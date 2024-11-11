package org.meliapp.backend.dto.purchase

import com.fasterxml.jackson.annotation.JsonProperty
import org.meliapp.backend.dto.product.ProductPurchaseResponse
import java.math.BigDecimal
import java.time.LocalDateTime

data class PurchaseResponse(
    val id: Long,
    val quantity: Int,
    val product: ProductPurchaseResponse,
    @JsonProperty("created_at")
    val createdAt: LocalDateTime,
    val total: BigDecimal,
)
