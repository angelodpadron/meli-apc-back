package org.meliapp.backend.dto.product

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

data class ProductDetailsResponse(
    @JsonProperty("id")
    val meliId: String,
    val title: String,
    val price: BigDecimal,
    val thumbnail: String,
    val pictures: List<PicturesListResponse>,
    var description: String?,

    )

data class DescriptionResponse(
    @JsonProperty("plain_text")
    val description: String
)

data class PicturesListResponse @JsonCreator constructor(
    val url: String,
)