package org.meliapp.backend.dto.bookmark

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class BookmarkSummary @JsonCreator constructor(
    val id: Long,
    @JsonProperty(value = "product_title")
    val productTitle: String,
    val stars: Int,
    val thumbnail: String,
)
