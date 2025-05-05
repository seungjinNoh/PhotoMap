package com.example.network.repository

import com.example.model.w3w.W3WResponse
import javax.inject.Inject

class FakeW3WDataSource @Inject constructor() : W3WDataSource {

    override suspend fun get3WordAddress(latitude: Double, longitude: Double): W3WResponse {
        //todo 데이터 여러개 준비
        return W3WResponse(words = "책상/컴퓨터/음료수")
    }

}