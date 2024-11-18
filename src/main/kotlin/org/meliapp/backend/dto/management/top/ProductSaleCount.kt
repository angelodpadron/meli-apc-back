package org.meliapp.backend.dto.management.top

import com.fasterxml.jackson.annotation.JsonProperty

data class ProductSaleCount(
    @JsonProperty("meli_id")
    val meliId: String,
    val title: String,
    val sales: Long
)
