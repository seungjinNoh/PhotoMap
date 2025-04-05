package com.example.network.repository

import com.example.model.w3w.W3WResponse
import com.example.network.BuildConfig
import com.example.network.api.W3WApiService
import javax.inject.Inject

class DefaultW3WDataSource @Inject constructor(
    private val api: W3WApiService
) : W3WDataSource {

    override suspend fun get3WordAddress(latitude: Double, longitude: Double): W3WResponse {
        val cord = "$latitude,$longitude"
        val response = api.convertTo3Words(
            coordinate = cord,
            apiKey = BuildConfig.W3W_API_KEY
        )
        return response
    }

}