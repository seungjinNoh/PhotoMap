package com.example.home.model

import com.example.model.photo.PhotoUiModel

sealed interface HomeUiState {

    object Loading : HomeUiState
    data class Success(val photos: List<PhotoUiModel>) : HomeUiState

}