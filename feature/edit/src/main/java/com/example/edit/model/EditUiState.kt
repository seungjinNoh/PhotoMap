package com.example.edit.model

import com.example.model.photo.PhotoUiModel

sealed interface EditUiState {

    data object Loading : EditUiState

    data class Error(val message: String) : EditUiState

    data class Success(
        val photoUiModel: PhotoUiModel
    ) : EditUiState

}