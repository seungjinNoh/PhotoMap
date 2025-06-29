package com.example.search.model

import com.example.model.photo.PhotoInfo

sealed interface SearchUiState {
    object Loading : SearchUiState
    data class Success(
        val query: String = "",
        val filteredPhotos: List<PhotoInfo> = emptyList()
    ) : SearchUiState
}