package com.example.data.impl

import com.example.data.database.PhotoDao
import com.example.data.entity.PhotoEntity
import com.example.domain.model.Photo
import com.example.domain.repository.PhotoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    private val dao: PhotoDao
) : PhotoRepository {

    override fun getAllPhotos(): Flow<List<Photo>> {
        return dao.getAllPhoto()
            .map { list -> list.map { it.toDomain() } }
    }

    override suspend fun insertPhoto(photo: Photo) {
        dao.insert(PhotoEntity.fromDomain(photo))
    }

    override suspend fun deleteById(id: Long) {
        dao.deleteById(id)
    }

}