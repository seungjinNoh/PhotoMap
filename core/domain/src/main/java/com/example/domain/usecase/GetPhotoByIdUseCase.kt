package com.example.domain.usecase

import com.example.domain.model.Photo
import com.example.domain.repository.PhotoRepository
import javax.inject.Inject

class GetPhotoByIdUseCase @Inject constructor(
    private val repository: PhotoRepository
)  {

    suspend operator fun invoke(id: Long): Photo? {
        return repository.getPhotoById(id)
    }

}