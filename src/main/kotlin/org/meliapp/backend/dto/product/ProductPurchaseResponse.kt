package org.meliapp.backend.dto.product

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

data class ProductPurchaseResponse(
    @JsonProperty("meli_id")
    val meliId: String,
    val title: String,
    val thumbnail: String,
    val price: BigDecimal,
)
