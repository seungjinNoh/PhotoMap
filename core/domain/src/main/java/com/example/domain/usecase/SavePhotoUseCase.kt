package com.example.domain.usecase

import com.example.domain.repository.PhotoRepository
import com.example.model.photo.PhotoInfo
import javax.inject.Inject

class SavePhotoUseCase @Inject constructor(
    private val photoRepository: PhotoRepository
) {

    suspend operator fun invoke(photoInfo: PhotoInfo) = photoRepository.insertPhoto(photoInfo)

}