package com.example.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.DeletePhotoUseCase
import com.example.domain.usecase.GetW3WUseCase
import com.example.domain.usecase.SavePhotoUseCase
import com.example.edit.model.EditUiState
import com.example.model.photo.PhotoUiModel
import com.example.model.photo.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(
    internal val getW3WUseCase: GetW3WUseCase,
    private val savePhotoUseCase: SavePhotoUseCase,
    private val deletePhotoUseCase: DeletePhotoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<EditUiState>(EditUiState.Loading)
    val uiState: StateFlow<EditUiState> = _uiState

    fun createNewPhoto() {
        _uiState.value = EditUiState.Success(
            PhotoUiModel()
        )
    }

    fun editExistingPhoto(photoUiModel: PhotoUiModel) {
        _uiState.value = EditUiState.Success(photoUiModel)
    }

    fun updateTitle(title: String) {
        _uiState.update { current ->
            when (current) {
                is EditUiState.Success -> current.copy(
                    photoUiModel = current.photoUiModel.copy(title = title)
                )
                else -> current
            }
        }
    }

    fun updateDescription(description: String) {
        _uiState.update { current ->
            when (current) {
                is EditUiState.Success -> current.copy(
                    photoUiModel = current.photoUiModel.copy(description = description)
                )
                else -> current
            }
        }
    }

    fun updateTags(tags: List<String>) {
        _uiState.update { current ->
            when (current) {
                is EditUiState.Success -> current.copy(
                    photoUiModel = current.photoUiModel.copy(tags = tags)
                )
                else -> current
            }
        }
    }

    fun updateW3W(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            getW3WUseCase(latitude, longitude)
                .collect { response ->
                    _uiState.update { current ->
                        when (current) {
                            is EditUiState.Success -> current.copy(
                                photoUiModel = current.photoUiModel.copy(
                                    w3w = response.words
                                )
                            )
                            else -> current
                        }
                    }
                }
        }
    }

    fun updateLocation(latitude: Double, longitude: Double, w3w: String) {
        _uiState.update { current ->
            when (current) {
                is EditUiState.Success -> current.copy(
                    photoUiModel = current.photoUiModel.copy(
                        latitude = latitude, longitude = longitude, w3w = w3w
                    )
                )
                else -> current
            }
        }
    }

    fun updatePhotoUri(uri: String) {
        _uiState.update { current ->
            if (current is EditUiState.Success) {
                current.copy(photoUiModel = current.photoUiModel.copy(photoUri = uri))
            } else current
        }
    }

    fun savePhoto(onSaved: () -> Unit) {
        val photoUiModel = (uiState.value as? EditUiState.Success)?.photoUiModel ?: return

        viewModelScope.launch {
            savePhotoUseCase(photoUiModel.toDomain())
            onSaved() // 저장 후 화면 닫기 등 처리
        }
    }

    fun deletePhoto(id: Long?, onDeleted: () -> Unit) {
        if (id != null) {
            viewModelScope.launch {
                val current = uiState.value
                if (current is EditUiState.Success) {
                    // 실제 삭제 처리
                    deletePhotoUseCase(id) // 실제 구현에 따라 변경
//                    _uiState.value = EditUiState.Deleted
                    onDeleted()
                }
            }
        }

    }

}