package org.meliapp.backend.dto.management.top

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

data class UserPurchaseCount(
    val email: String,
    @JsonProperty("total_spend")
    val totalSpend: BigDecimal,
    @JsonProperty("total_items")
    val totalItems: Long
)
