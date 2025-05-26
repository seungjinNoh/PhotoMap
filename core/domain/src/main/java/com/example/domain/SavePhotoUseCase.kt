package com.example.domain

import com.example.data.entity.PhotoEntity
import com.example.data.repository.PhotoRepository
import com.example.model.photo.PhotoInfo
import javax.inject.Inject

class SavePhotoUseCase @Inject constructor(
    private val photoRepository: PhotoRepository
) {

    suspend operator fun invoke(photoInfo: PhotoInfo) = photoRepository.insertPhoto(PhotoEntity.fromDomain(photoInfo))

}