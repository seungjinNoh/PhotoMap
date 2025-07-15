package com.example.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GetAllPhotoUseCase
import com.example.map.model.MapUiState
import com.example.model.photo.PhotoUiModel
import com.example.model.photo.toUiModel
import com.google.android.gms.maps.model.BitmapDescriptor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getAllPhotoUseCase: GetAllPhotoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<MapUiState>(MapUiState.Loading)
    val uiState: StateFlow<MapUiState> = _uiState

    private val _markerIcon = MutableStateFlow<Map<Long, BitmapDescriptor?>>(emptyMap())
    val markerIcon: StateFlow<Map<Long, BitmapDescriptor?>> = _markerIcon

    init {
        viewModelScope.launch {
            getAllPhotoUseCase()
                .collect { photoList ->
                    if (photoList.isNotEmpty()) {
                        _uiState.value = MapUiState.Success(
                            photoUiModelList = photoList.map { it.toUiModel() },
                            selectedPhoto = null
                        )
                    }
                }
        }
    }

    fun selectPhoto(photoUiModel: PhotoUiModel?) {
        val currentState = _uiState.value
        if (currentState is MapUiState.Success) {
            _uiState.value = currentState.copy(selectedPhoto = photoUiModel)
        }
    }

    fun setMarkerIcons(icons: Map<Long, BitmapDescriptor?>) {
        _markerIcon.value = icons
    }

}