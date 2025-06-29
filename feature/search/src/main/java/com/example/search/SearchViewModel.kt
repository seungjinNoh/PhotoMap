package com.example.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.GetAllPhotoUseCase
import com.example.model.photo.PhotoInfo
import com.example.search.model.SearchUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getAllPhotoUseCase: GetAllPhotoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Loading)
    val uiState: StateFlow<SearchUiState> = _uiState

    private var allPhotos: List<PhotoInfo> = emptyList()

    init {
        viewModelScope.launch {
            getAllPhotoUseCase().collect { photos ->
                allPhotos = photos
                _uiState.value = SearchUiState.Success(
                    query = "",
                    filteredPhotos = photos
                )
            }
        }
    }

    fun updateQuery(query: String) {
        val current = _uiState.value
        if (current is SearchUiState.Success) {
            _uiState.value = current.copy(
                query = query,
                filteredPhotos = allPhotos.filter {
                    it.title.contains(query, true) ||
                            it.tags.any { tag -> tag.contains(query, true) }
                }
            )
        }
    }
}