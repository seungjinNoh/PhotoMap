package com.example.domain

import com.example.data.repository.PhotoRepository
import javax.inject.Inject

class DeletePhotoUseCase @Inject constructor(
    private val photoRepository: PhotoRepository
) {

    suspend operator fun invoke(id: Long) = photoRepository.deleteById(id)

}