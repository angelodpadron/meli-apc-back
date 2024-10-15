package org.meliapp.backend.dto.bookmark

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class BookmarkRequestBody @JsonCreator constructor(
    @JsonProperty(value = "meli_id")
    val meliId: String,
    val stars: Int,
    val comment: String
)