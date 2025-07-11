package com.example.home.model

import com.example.model.photo.PhotoUiModel

sealed interface HomeUiState {

    object Loading : HomeUiState
    object Empty : HomeUiState
    data class Success(val photos: List<PhotoUiModel>) : HomeUiState

}