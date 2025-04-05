package com.example.model.w3w

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class W3WResponse(
    @SerialName("words") val words: String
)

