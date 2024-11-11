package org.meliapp.backend.dto.bookmark

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class BookmarkDetails @JsonCreator constructor(
    val id: Long,
    @JsonProperty(value = "product_title")
    val productTitle: String,
    @JsonProperty(value = "post_id")
    val postId: String,
    val thumbnail: String,
    val stars: Int,
    val comment: String,

)