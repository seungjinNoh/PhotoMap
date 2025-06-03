package com.example.home.model

import com.example.model.photo.PhotoInfo

sealed interface HomeUiState {

    object Loading : HomeUiState
    data class Success(val photos: List<PhotoInfo>) : HomeUiState

}