package org.meliapp.backend.dto.management

import com.fasterxml.jackson.annotation.JsonProperty

data class BookmarkBasicResume(
    @JsonProperty("mali_id")
    val meliId: String,
    val title: String,
    val quantity: Long
)
