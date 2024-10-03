package org.meliapp.backend.dto

data class ApiResponse<T>(
    val payload: T? = null,
)
