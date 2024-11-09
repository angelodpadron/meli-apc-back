package org.meliapp.backend.dto.purchase

import com.fasterxml.jackson.annotation.JsonProperty

data class PurchaseRequest(
    @JsonProperty(value = "meli_id")
    val meliId: String,
    val quantity: Int
)
