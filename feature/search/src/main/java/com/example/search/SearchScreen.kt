package com.example.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.example.designsystem.theme.PhotoMapTheme
import com.example.model.photo.PhotoUiModel
import com.example.search.model.SearchUiState

@Composable
fun SearchScreen(
    onEditClick: (Long) -> Unit,
    padding: PaddingValues,
    viewModel: SearchViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .background(color = PhotoMapTheme.colors.background)
    ) {
        SearchTopBar()

        when (uiState) {
            is SearchUiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("로딩 중...", color = PhotoMapTheme.colors.textTitle)
                }
            }

            is SearchUiState.Success -> {
                val state = uiState as SearchUiState.Success

                OutlinedTextField(
                    value = state.query,
                    onValueChange = viewModel::updateQuery,
                    label = { Text("검색", color = PhotoMapTheme.colors.editTag) },
                    placeholder = { Text("제목, 태그를 입력하세요.", color = PhotoMapTheme.colors.editText.copy(alpha = 0.6f)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PhotoMapTheme.colors.button,
                        unfocusedBorderColor = PhotoMapTheme.colors.editText.copy(alpha = 0.3f),
                        focusedTextColor = PhotoMapTheme.colors.editText,
                        unfocusedTextColor = PhotoMapTheme.colors.editText,
                        focusedLabelColor = PhotoMapTheme.colors.button,
                        unfocusedLabelColor = PhotoMapTheme.colors.editTag
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // query가 비어있지 않을 때만 리스트 표시
                if (state.query.isNotBlank()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.filteredPhotos) { photo ->
                            SearchPhotoCard(photo = photo, onClick = { photo.id?.let { onEditClick(it) } })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "사진 찾기",
            style = MaterialTheme.typography.titleLarge,
            color = PhotoMapTheme.colors.textTitle
        )
    }
}

@Composable
fun SearchPhotoCard(
    photo: PhotoUiModel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = PhotoMapTheme.colors.background
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(photo.photoUri),
                contentDescription = "사진",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = photo.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = PhotoMapTheme.colors.textTitle
                )

                if (photo.tags.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(photo.tags) { tag ->
                            SearchTag(tag = tag)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchTag(tag: String) {
    val tagShape = RoundedCornerShape(12.dp)
    val tagColor = PhotoMapTheme.colors.button

    Box(
        modifier = Modifier
            .background(tagColor.copy(alpha = 0.1f), tagShape)
            .border(1.dp, tagColor.copy(alpha = 0.3f), tagShape)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = "#$tag",
            style = MaterialTheme.typography.bodySmall,
            color = PhotoMapTheme.colors.editText
        )
    }
}