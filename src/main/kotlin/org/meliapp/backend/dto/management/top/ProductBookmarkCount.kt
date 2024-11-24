package org.meliapp.backend.dto.management.top

import com.fasterxml.jackson.annotation.JsonProperty

data class ProductBookmarkCount(
    @JsonProperty("meli_id")
    val meliId: String,
    val title: String,
    val quantity: Long
)
