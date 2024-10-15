package org.meliapp.backend.dto.product

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

data class ProductResponse @JsonCreator constructor(
    @JsonProperty("id")
    val id: String = "",
    @JsonProperty("title")
    val title: String = "",
    @JsonProperty("price")
    val price: BigDecimal,
    @JsonProperty("thumbnail")
    val thumbnail: String = "",
    @JsonProperty("available_quantity")
    val availableQuantity: Int = 0
)
