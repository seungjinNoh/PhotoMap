package com.example.edit.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.example.model.photo.PhotoInfo

@Stable
sealed interface EditUiState {

    @Immutable
    data object Loading : EditUiState

    @Immutable
    data object Empty : EditUiState

    @Immutable
    data class Success(
        val photoInfo: PhotoInfo
    ) : EditUiState

}