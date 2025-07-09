package com.example.model.photo

import com.example.domain.model.Photo
import kotlinx.serialization.Serializable

@Serializable
data class PhotoUiModel(
    val id: Long? = null,
    val title: String = "",
    val tags: List<String> = emptyList(),
    val description: String = "",
    val photoUri: String = "",
    val w3w: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)

fun Photo.toUiModel(): PhotoUiModel {
    return PhotoUiModel(
        id = this.id,
        title = this.title,
        tags = this.tags,
        description = this.description,
        photoUri = this.photoUri,
        w3w = this.w3w,
        latitude = this.latitude,
        longitude = this.longitude
    )
}

fun List<Photo>.toUiModelList(): List<PhotoUiModel> = map { it.toUiModel() }

fun PhotoUiModel.toDomain(): Photo {
    return Photo(
        id = this.id ?: 0L,
        title = this.title,
        tags = this.tags,
        description = this.description,
        photoUri = this.photoUri,
        w3w = this.w3w,
        latitude = this.latitude,
        longitude = this.longitude
    )
}
