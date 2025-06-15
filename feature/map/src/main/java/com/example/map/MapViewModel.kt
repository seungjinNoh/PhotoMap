package com.example.map

import android.provider.ContactsContract.Contacts.Photo
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.GetAllPhotoUseCase
import com.example.map.model.MapUiState
import com.example.model.photo.PhotoInfo
import com.google.android.gms.maps.model.BitmapDescriptor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getAllPhotoUseCase: GetAllPhotoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<MapUiState>(MapUiState.Loading)
    val uiState: StateFlow<MapUiState> = _uiState

    private val _markerIcon = MutableStateFlow<List<Map<Long, BitmapDescriptor?>>>(emptyList())
    val markerIcon: StateFlow<List<Map<Long, BitmapDescriptor?>>> = _markerIcon

    private val _requestedPhotoMarker = MutableSharedFlow<List<PhotoInfo>>()
    val requestedPhotoMarker: SharedFlow<List<PhotoInfo>> = _requestedPhotoMarker.asSharedFlow()

    init {
        viewModelScope.launch {
            getAllPhotoUseCase()
                .collect { photoList ->
                    if (photoList.isNotEmpty()) {
                        _uiState.value = MapUiState.Success(
                            photoList = photoList,
                            selectedPhoto = null
                        )
                    }
                }
        }
    }

    fun selectPhoto(photo: PhotoInfo?) {
        val currentState = _uiState.value
        if (currentState is MapUiState.Success) {
            _uiState.value = currentState.copy(selectedPhoto = photo)
        }
    }

    fun setMarkerIcons(icons: List<Map<Long, BitmapDescriptor?>>) {
        _markerIcon.value = icons
    }

    fun requestPhotoMarker(photos: List<PhotoInfo>) {
        viewModelScope.launch {
            _requestedPhotoMarker.emit(photos)
        }
    }

}