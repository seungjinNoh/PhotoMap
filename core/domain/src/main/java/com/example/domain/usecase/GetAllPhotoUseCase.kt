package com.example.domain.usecase

import com.example.domain.repository.PhotoRepository
import com.example.model.photo.PhotoInfo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllPhotoUseCase @Inject constructor(
    private val repository: PhotoRepository
) {

    operator fun invoke(): Flow<List<PhotoInfo>> {
        return repository.getAllPhotos()
    }

}