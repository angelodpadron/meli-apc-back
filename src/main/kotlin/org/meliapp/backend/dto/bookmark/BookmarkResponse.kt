package org.meliapp.backend.dto.bookmark

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class BookmarkResponse @JsonCreator constructor(
    val id: Long,
    @JsonProperty(value = "meli_id")
    val meliId: String,
    @JsonProperty(value = "user_id")
    val userId: Long,
    val stars: Int,
    val comment: String
)
