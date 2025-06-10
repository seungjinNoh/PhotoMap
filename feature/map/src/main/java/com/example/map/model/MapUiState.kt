package com.example.map.model

import com.example.model.photo.PhotoInfo

sealed class MapUiState {
    data object Loading : MapUiState()
    data class Success(
        val photoList: List<PhotoInfo>,
        val selectedPhoto: PhotoInfo?
    ) : MapUiState()
}

sealed class BottomBarState {
    data class Default(val photoInfo: PhotoInfo) : BottomBarState()
}