package com.example.edit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.GetW3WUseCase
import com.example.edit.model.EditUiState
import com.example.model.photo.PhotoInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(
    private val getW3WUseCase: GetW3WUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<EditUiState>(EditUiState.Loading)
    val uiState: StateFlow<EditUiState> = _uiState

    fun createNewPhoto() {
        _uiState.value = EditUiState.Empty
    }

    fun editExistingPhoto(photoInfo: PhotoInfo) {
        _uiState.value = EditUiState.Success(photoInfo)
    }

    fun updateTitle(title: String) {
        _uiState.update { current ->
            when (current) {
                is EditUiState.Success -> current.copy(
                    photoInfo = current.photoInfo.copy(title = title)
                )
                else -> current
            }
        }
    }

    fun updateDescription(description: String) {
        _uiState.update { current ->
            when (current) {
                is EditUiState.Success -> current.copy(
                    photoInfo = current.photoInfo.copy(description = description)
                )
                else -> current
            }
        }
    }

    fun updateTags(tags: List<String>) {
        _uiState.update { current ->
            when (current) {
                is EditUiState.Success -> current.copy(
                    photoInfo = current.photoInfo.copy(tags = tags)
                )
                else -> current
            }
        }
    }

    fun updateLocation(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            getW3WUseCase(latitude, longitude)
                .collect { response ->
                    _uiState.update { current ->
                        when (current) {
                            is EditUiState.Success -> current.copy(
                                photoInfo = current.photoInfo.copy(
                                    latitude = latitude,
                                    longitude = longitude,
                                    w3w = response.words  // << 예를 들면 이렇게. (w3w 주소 필드 이름에 따라 수정)
                                )
                            )
                            else -> current
                        }
                    }
                }
        }
    }

}