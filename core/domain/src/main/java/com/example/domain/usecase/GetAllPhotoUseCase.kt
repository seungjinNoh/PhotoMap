package com.example.domain.usecase

import com.example.domain.model.Photo
import com.example.domain.repository.PhotoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllPhotoUseCase @Inject constructor(
    private val repository: PhotoRepository
) {

    operator fun invoke(): Flow<List<Photo>> {
        return repository.getAllPhotos()
    }

}