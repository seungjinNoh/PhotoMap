package com.example.domain.repository

import com.example.domain.model.W3W
import kotlinx.coroutines.flow.Flow

interface W3WRepository {
    fun get3WordAddress(latitude: Double, longitude: Double) : Flow<W3W>
}