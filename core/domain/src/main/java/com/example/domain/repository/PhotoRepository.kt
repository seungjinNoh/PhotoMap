package com.example.domain.repository

import com.example.domain.model.Photo
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {
    fun getAllPhotos(): Flow<List<Photo>>
    suspend fun insertPhoto(photo: Photo)
    suspend fun deleteById(id: Long)
}