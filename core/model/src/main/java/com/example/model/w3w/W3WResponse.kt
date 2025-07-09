package com.example.model.w3w

import com.example.domain.model.W3W
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class W3WResponse(
    @SerialName("words") val words: String
)

fun W3WResponse.toDomain(): W3W {
    return W3W(words = this.words)
}

