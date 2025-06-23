package com.example.data.repository

import com.example.data.entity.PhotoEntity
import com.example.model.photo.PhotoInfo
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {
    fun getAllPhotos(): Flow<List<PhotoInfo>>
    suspend fun insertPhoto(photo: PhotoEntity)
    suspend fun deleteById(id: Long)
}