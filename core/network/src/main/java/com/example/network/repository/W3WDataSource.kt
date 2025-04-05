package com.example.network.repository

import com.example.model.w3w.W3WResponse

interface W3WDataSource {

    suspend fun get3WordAddress(latitude: Double, longitude: Double): W3WResponse

}