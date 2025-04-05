package com.example.data.repository

import com.example.model.w3w.W3WResponse
import com.example.network.repository.W3WDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DefaultW3WRepository @Inject constructor(
    private val w3WDataSource: W3WDataSource
) : W3WRepository {

    override fun get3WordAddress(latitude: Double, longitude: Double): Flow<W3WResponse> = flow {
        val response = w3WDataSource.get3WordAddress(latitude, longitude)
        emit(response)
    }.catch { e ->
        // 필요한 에러 처리 가능
        throw e
    }.flowOn(Dispatchers.IO) // 네트워크는 IO에서 처리

}