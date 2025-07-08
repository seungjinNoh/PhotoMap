package com.example.domain.repository

import com.example.model.w3w.W3WResponse
import kotlinx.coroutines.flow.Flow

interface W3WRepository {
    fun get3WordAddress(latitude: Double, longitude: Double) : Flow<W3WResponse>
}