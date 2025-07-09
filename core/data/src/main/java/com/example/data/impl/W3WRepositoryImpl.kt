package com.example.data.impl

import com.example.domain.model.W3W
import com.example.domain.repository.W3WRepository
import com.example.model.w3w.toDomain
import com.example.network.repository.W3WDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class W3WRepositoryImpl @Inject constructor(
    private val w3WDataSource: W3WDataSource
) : W3WRepository {

    override fun get3WordAddress(latitude: Double, longitude: Double): Flow<W3W> = flow {
        val response = w3WDataSource.get3WordAddress(latitude, longitude).toDomain()
        emit(response)
    }.catch { e ->
        throw e
    }.flowOn(Dispatchers.IO)

}