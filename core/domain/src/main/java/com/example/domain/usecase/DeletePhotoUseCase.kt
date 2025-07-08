package com.example.domain.usecase

import com.example.domain.repository.PhotoRepository
import javax.inject.Inject

class DeletePhotoUseCase @Inject constructor(
    private val photoRepository: PhotoRepository
) {

    suspend operator fun invoke(id: Long) = photoRepository.deleteById(id)

}