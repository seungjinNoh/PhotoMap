@file:Suppress("INFERRED_TYPE_VARIABLE_INTO_EMPTY_INTERSECTION_WARNING")

package com.example.edit

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import coil.compose.rememberAsyncImagePainter
import com.example.edit.model.EditUiState
import com.example.model.photo.PhotoUiModel
import com.example.navigation.Route

@Composable
fun EditScreen(
    photoUiModel: PhotoUiModel?,
    onBackClick: () -> Unit,
    onSelectLocationClick: (Route.SelectLocation) -> Unit,
    viewModel: EditViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    // 초기 진입 처리: null이면 새로 작성, 아니면 기존 정보로 수정
    LaunchedEffect(Unit) {
        when (uiState) {
            is EditUiState.Loading -> {
                if (photoUiModel == null) viewModel.createNewPhoto()
                else viewModel.editExistingPhoto(photoUiModel)
            }
            else -> Unit
        }
    }


    Column(modifier = Modifier.fillMaxSize()) {
        EditTopBar(
            photoUiModel,
            onBackClick = onBackClick,
            onSaveClick= { viewModel.savePhoto(onBackClick) },
            onDeleteClick = { viewModel.deletePhoto(photoUiModel?.id, onBackClick) }
        )

        Spacer(modifier = Modifier.height(4.dp))

        if (uiState is EditUiState.Success) {
            val photo = (uiState as EditUiState.Success).photoUiModel
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
                photoUri = photo.photoUri,
                onRemoveTag = { tag -> viewModel.updateTags(photo.tags - tag) },
                description = photo.description,
                onDescriptionChange = viewModel::updateDescription,
                onSelectLocationClick = onSelectLocationClick,
                updatePhotoUri = viewModel::updatePhotoUri
            )
        } else {
            Text("Loading...")
        }
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
    photoUri: String = "",
    onRemoveTag: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    onSelectLocationClick: (Route.SelectLocation) -> Unit,
    updatePhotoUri: (String) -> Unit
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
        ImagePicker(
            currentUri = photoUri,
            onImageSelected = updatePhotoUri
        )
    }
}

@Composable
fun EditTopBar(
    photoUiModel: PhotoUiModel?,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("사진 삭제") },
            text = { Text("정말로 이 사진을 삭제하시겠습니까?") },
            confirmButton = {
                Text(
                    text = "삭제",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .clickable {
                            showDeleteDialog = false
                            onDeleteClick()
                        }
                        .padding(16.dp)
                )
            },
            dismissButton = {
                Text(
                    text = "취소",
                    modifier = Modifier
                        .clickable { showDeleteDialog = false }
                        .padding(16.dp)
                )
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "뒤로가기",
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 12.dp)
                .size(24.dp)
                .clickable(onClick = onBackClick)
        )

        Text(
            text = if (photoUiModel != null) "편집하기" else "추가하기",
            modifier = Modifier.align(Alignment.Center),
            style = androidx.compose.material3.MaterialTheme.typography.titleMedium
        )

        Row(modifier = Modifier.align(Alignment.CenterEnd)) {
            if (photoUiModel != null) {
                Text(
                    text = "삭제",
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .clickable{ showDeleteDialog = true },
                    color = MaterialTheme.colorScheme.error
                )
            }

            Text(
                text = "저장",
                modifier = Modifier
                    .padding(end = 12.dp)
                    .clickable(onClick = onSaveClick),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun ImagePicker(
    currentUri: String?,
    onImageSelected: (String) -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onImageSelected(it.toString()) }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // 상단 제목
        Text(
            text = "사진",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 이미지 또는 아이콘
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clickable { launcher.launch("image/*") }
                .background(Color.LightGray.copy(alpha = 0.2f), shape = RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (!currentUri.isNullOrBlank()) {
                Image(
                    painter = rememberAsyncImagePainter(currentUri),
                    contentDescription = "선택한 사진",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .border(2.dp, Color.Gray, shape = CircleShape)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = "기본 카메라 아이콘",
                        modifier = Modifier.size(40.dp),
                        tint = Color.Gray
                    )
                }
            }
        }
    }
}
