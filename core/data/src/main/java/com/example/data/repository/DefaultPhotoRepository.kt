package com.example.data.repository

import com.example.data.database.PhotoDao
import com.example.data.entity.PhotoEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultPhotoRepository @Inject constructor(
    private val dao: PhotoDao
) : PhotoRepository {

    override fun getAllPhotos(): Flow<List<PhotoEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertPhoto(photo: PhotoEntity) {
        dao.insert(photoEntity = photo)
    }

}