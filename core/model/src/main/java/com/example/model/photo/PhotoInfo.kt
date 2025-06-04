package com.example.model.photo

import kotlinx.serialization.Serializable

@Serializable
data class PhotoInfo(
    val id: Long? = null,
    val title: String = "",
    val tags: List<String> = emptyList(),
    val description: String = "",
    val photoUri: String = "",
    val w3w: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)
