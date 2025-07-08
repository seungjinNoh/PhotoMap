package com.example.domain.repository

import com.example.model.photo.PhotoInfo
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {
    fun getAllPhotos(): Flow<List<PhotoInfo>>
    suspend fun insertPhoto(photo: PhotoInfo)
    suspend fun deleteById(id: Long)
}