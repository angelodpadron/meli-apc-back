package org.meliapp.backend.dto.meli

data class SearchFilter(
    val id: String,
    val type: String,
    val name: String,
    val values: List<Map<Any, Any>>
)
