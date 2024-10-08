package org.meliapp.backend.dto.auth

import com.fasterxml.jackson.annotation.JsonCreator

data class AuthRequestBody @JsonCreator constructor(
    val email: String,
    val password: String
)
