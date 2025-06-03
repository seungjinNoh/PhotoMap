package com.example.data.repository

import com.example.data.database.PhotoDao
import com.example.data.entity.PhotoEntity
import com.example.model.photo.PhotoInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultPhotoRepository @Inject constructor(
    private val dao: PhotoDao
) : PhotoRepository {

    override fun getAllPhotos(): Flow<List<PhotoInfo>> {
        return dao.getAllPhoto()
            .map { list -> list.map { it.toDomain() } }
    }

    override suspend fun insertPhoto(photo: PhotoEntity) {
        dao.insert(photoEntity = photo)
    }

}