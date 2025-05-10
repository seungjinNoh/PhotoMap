@file:Suppress("INFERRED_TYPE_VARIABLE_INTO_EMPTY_INTERSECTION_WARNING")

package com.example.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.edit.model.EditUiState
import com.example.model.photo.PhotoInfo
import com.example.navigation.Route

@Composable
fun EditScreen(
    photoInfo: PhotoInfo?,
    onBackClick: () -> Unit,
    onSelectLocationClick: (Route.SelectLocation) -> Unit,
    viewModel: EditViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    // 초기 진입 처리: null이면 새로 작성, 아니면 기존 정보로 수정
    LaunchedEffect(Unit) {
        when (uiState) {
            is EditUiState.Loading -> {
                if (photoInfo == null) viewModel.createNewPhoto()
                else viewModel.editExistingPhoto(photoInfo)
            }
            else -> Unit
        }
    }

    if (uiState is EditUiState.Success) {
        val photo = (uiState as EditUiState.Success).photoInfo
        var tagInput by remember { mutableStateOf("") }

        EditContent(
            title = photo.title,
            onTitleChange = viewModel::updateTitle,
            tagInput = tagInput,
            onTagInputChange = { tagInput = it },
            onAddTag = {
                if (tagInput.isNotBlank()) {
                    viewModel.updateTags(photo.tags + tagInput.trim())
                    tagInput = ""
                }
            },
            tags = photo.tags,
            w3w = photo.w3w,
            onRemoveTag = { tag -> viewModel.updateTags(photo.tags - tag) },
            description = photo.description,
            onDescriptionChange = viewModel::updateDescription,
            onSelectLocationClick = onSelectLocationClick
        )
    } else {
        // 로딩 중 혹은 상태 없음
        Text("Loading...")
    }
}

@Composable
fun EditContent(
    title: String,
    onTitleChange: (String) -> Unit,
    tagInput: String,
    onTagInputChange: (String) -> Unit,
    onAddTag: () -> Unit,
    tags: List<String>,
    w3w: String? = null,
    onRemoveTag: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    onSelectLocationClick: (Route.SelectLocation) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            label = { Text("제목") },
            placeholder = { Text("제목을 입력하세요.") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = tagInput,
                onValueChange = onTagInputChange,
                label = { Text("태그") },
                placeholder = { Text("태그를 추가하세요.") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onAddTag) {
                Text("추가")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(tags) { tag ->
                Box(
                    modifier = Modifier
                        .background(Color.LightGray, shape = CircleShape)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(tag)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "태그 삭제",
                            modifier = Modifier
                                .size(16.dp)
                                .clickable { onRemoveTag(tag) }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("설명") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(16.dp))

        // W3W 주소 입력 + 지도 아이콘
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            val dummyInteraction = remember { MutableInteractionSource() }
            OutlinedTextField(
                value = w3w ?: "지도에서 위치를 선택하세요.",
                onValueChange = {},  // 입력 차단
                readOnly = true,     // 키보드 안 뜨게
                interactionSource = dummyInteraction, // 클릭 효과 제거
                label = { Text("w3w") },
                placeholder = { Text("w3w 주소") },
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 56.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "지도에서 선택",
                modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        val latitude = 0.0
                        val longitude = 0.0
                        onSelectLocationClick(Route.SelectLocation(latitude, longitude))
                    }
            )
        }

        var currentLocationChecked by remember { mutableStateOf(false) }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = (-6).dp, y = (-6).dp)

        ) {
            Checkbox(
                checked = currentLocationChecked,
                onCheckedChange = { currentLocationChecked = it }
            )

            Text(
                text = "현재 위치로 설정",
                modifier = Modifier
                    .clickable { currentLocationChecked = !currentLocationChecked }
            )
        }

    }
}
