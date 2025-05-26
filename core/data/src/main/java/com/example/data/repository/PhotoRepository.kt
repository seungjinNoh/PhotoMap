package com.example.data.repository

import com.example.data.entity.PhotoEntity
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {
    fun getAllPhotos(): Flow<List<PhotoEntity>>
    suspend fun insertPhoto(photo: PhotoEntity)
}