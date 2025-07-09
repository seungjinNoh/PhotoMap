package com.example.map.model

import com.example.model.photo.PhotoUiModel

sealed class MapUiState {
    data object Loading : MapUiState()
    data class Success(
        val photoUiModelList: List<PhotoUiModel>,
        val selectedPhoto: PhotoUiModel?
    ) : MapUiState()
}

sealed class BottomBarState {
    data class Default(val photoUiModel: PhotoUiModel) : BottomBarState()
}