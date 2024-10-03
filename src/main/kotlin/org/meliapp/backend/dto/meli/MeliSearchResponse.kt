package org.meliapp.backend.dto.meli

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import org.meliapp.backend.dto.product.ProductResponse

data class MeliSearchResponse @JsonCreator constructor(
    val results: List<ProductResponse> = emptyList(),
    val filters: List<SearchFilter> = emptyList(),
    @JsonProperty(value = "available_filters")
    val availableFilters: List<SearchFilter> = emptyList(),
)
