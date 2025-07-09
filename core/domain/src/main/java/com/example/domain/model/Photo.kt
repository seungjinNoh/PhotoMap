package com.example.domain.model

data class Photo(
    val id: Long,
    val title: String,
    val tags: List<String>,
    val description: String,
    val photoUri: String,
    val w3w: String?,
    val latitude: Double?,
    val longitude: Double?
)

