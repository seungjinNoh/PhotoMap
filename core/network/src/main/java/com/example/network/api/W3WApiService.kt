package com.example.network.api

import com.example.model.w3w.W3WResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface W3WApiService {

    @GET("v4/convert-to-3wa")
    suspend fun convertTo3Words(
        @Query("coordinates") coordinate: String,
        @Query("key") apiKey: String
    ) : W3WResponse

}