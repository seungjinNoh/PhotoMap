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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.example.designsystem.theme.PhotoMapTheme
import com.example.edit.model.EditUiState
import com.example.model.photo.PhotoUiModel
import com.example.navigation.Route

@Composable
fun EditScreen(
    onBackClick: () -> Unit,
    onSelectLocationClick: (Route.SelectLocation) -> Unit,
    viewModel: EditViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    if (uiState is EditUiState.Error) {
        val error = (uiState as EditUiState.Error)
        errorMessage = error.message
        showErrorDialog = true
    }

    if (showErrorDialog) {
        AlertDialog(
            containerColor = PhotoMapTheme.colors.background,
            onDismissRequest = { showErrorDialog = false },
            title = { Text("오류 발생", color = PhotoMapTheme.colors.textTitle) },
            text = { Text(errorMessage) },
            confirmButton = {
                TextButton(onClick = {
                    showErrorDialog = false
                    onBackClick()
                }) {
                    Text("확인", color = PhotoMapTheme.colors.textTitle)
                }
            }
        )
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(color = PhotoMapTheme.colors.background)
    ) {
        Spacer(modifier = Modifier.height(4.dp))

        when (uiState) {
            is EditUiState.Success -> {
                val photo = (uiState as EditUiState.Success).photoUiModel
                var tagInput by remember { mutableStateOf("") }

                EditTopBar(
                    photoUiModel = photo,
                    onBackClick = onBackClick,
                    onSaveClick = { viewModel.savePhoto(onBackClick) },
                    onDeleteClick = {
                        if (photo.id != null) viewModel.deletePhoto(photo.id, onBackClick)
                    }
                )

                EditContent(
                    title = photo.title,
                    onTitleChange = { title -> viewModel.updateTitle(title) },
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
                    onDescriptionChange = { description -> viewModel.updateDescription(description) },
                    onSelectLocationClick = onSelectLocationClick,
                    updatePhotoUri = { uri -> viewModel.updatePhotoUri(uri) }
                )
            }

            is EditUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            else -> Unit
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
            label = { Text("제목", color = PhotoMapTheme.colors.editText) },
            placeholder = { Text("제목을 입력하세요.", color = PhotoMapTheme.colors.editText) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PhotoMapTheme.colors.editTextSelected,
                unfocusedBorderColor = PhotoMapTheme.colors.editText,
                focusedTextColor = PhotoMapTheme.colors.editText,
                unfocusedTextColor = PhotoMapTheme.colors.editText
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = tagInput,
                onValueChange = onTagInputChange,
                label = { Text("태그", color = PhotoMapTheme.colors.editTag) },
                placeholder = { Text("태그를 추가하세요.", color = PhotoMapTheme.colors.editText) },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PhotoMapTheme.colors.editTextSelected,
                    unfocusedBorderColor = PhotoMapTheme.colors.editText,
                    focusedTextColor = PhotoMapTheme.colors.editText,
                    unfocusedTextColor = PhotoMapTheme.colors.editText
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = onAddTag,
                colors = ButtonDefaults.buttonColors(
                    containerColor = PhotoMapTheme.colors.button
                )
            ) {
                Text("추가", color = PhotoMapTheme.colors.textTitle)
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
            label = { Text("설명", color = PhotoMapTheme.colors.editTag) },
            placeholder = { Text("설명을 입력하세요.", color = PhotoMapTheme.colors.editText) },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 5,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PhotoMapTheme.colors.editTextSelected,
                unfocusedBorderColor = PhotoMapTheme.colors.editText,
                focusedTextColor = PhotoMapTheme.colors.editText,
                unfocusedTextColor = PhotoMapTheme.colors.editText
            )
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
                label = { Text("w3w", color = PhotoMapTheme.colors.editTag) },
                placeholder = { Text("w3w 주소", color = PhotoMapTheme.colors.editText) },
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 56.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PhotoMapTheme.colors.editTextSelected,
                    unfocusedBorderColor = PhotoMapTheme.colors.editText,
                    focusedTextColor = PhotoMapTheme.colors.editText,
                    unfocusedTextColor = PhotoMapTheme.colors.editText
                )
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
                    },
                tint = PhotoMapTheme.colors.icon
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
    photoUiModel: PhotoUiModel,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            containerColor = PhotoMapTheme.colors.background,
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("사진 삭제", color = PhotoMapTheme.colors.textTitle) },
            text = { Text("정말로 이 사진을 삭제하시겠습니까?", color = PhotoMapTheme.colors.textTitle) },
            confirmButton = {
                Text(
                    text = "삭제",
                    color = PhotoMapTheme.colors.textTitle,
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
                    color = PhotoMapTheme.colors.textTitle,
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
                .clickable(onClick = onBackClick),
            tint = PhotoMapTheme.colors.icon
        )

        Text(
            text = if (photoUiModel.id != null) "편집하기" else "추가하기",
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.titleMedium,
            color = PhotoMapTheme.colors.textTitle
        )

        Row(modifier = Modifier.align(Alignment.CenterEnd)) {
            if (photoUiModel?.id != null) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "삭제",
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .clickable { showDeleteDialog = true }
                        .size(24.dp),
                    tint = PhotoMapTheme.colors.icon
                )
            }

            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "저장",
                modifier = Modifier
                    .padding(end = 12.dp)
                    .clickable(onClick = onSaveClick)
                    .size(24.dp),
                tint = PhotoMapTheme.colors.icon
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
            color = PhotoMapTheme.colors.textTitle,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 이미지 또는 아이콘
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clickable { launcher.launch("image/*") }
                .background(PhotoMapTheme.colors.photoSelect, shape = RoundedCornerShape(12.dp)),
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
                        tint = PhotoMapTheme.colors.icon
                    )
                }
            }
        }
    }
}
