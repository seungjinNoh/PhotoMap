package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.photo.PhotoInfo

@Entity(tableName = "photos")
data class PhotoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val tags: String,
    val description: String,
    val photoUri: String,
    val w3w: String?,
    val latitude: Double?,
    val longitude: Double?
) {
    fun toDomain() = PhotoInfo(
        title = title,
        tags = tags.split(","),
        description = description,
        photoUri = photoUri,
        w3w = w3w,
        latitude = latitude,
        longitude = longitude
    )

    companion object {
        fun fromDomain(info: PhotoInfo) = PhotoEntity(
            title = info.title,
            tags = info.tags.joinToString(","),
            description = info.description,
            photoUri = info.photoUri,
            w3w = info.w3w,
            latitude = info.latitude,
            longitude = info.longitude
        )
    }
}