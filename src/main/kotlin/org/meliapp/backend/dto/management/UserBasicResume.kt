package org.meliapp.backend.dto.management

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class UserBasicResume(
    val email: String,
    @JsonProperty("created_at")
    val createdAt: LocalDateTime
)
