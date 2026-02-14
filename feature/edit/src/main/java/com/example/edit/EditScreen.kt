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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.example.designsystem.theme.PhotoMapTheme
import com.example.edit.model.EditUiState
import com.example.model.photo.PhotoUiModel
import com.example.navigation.EditRoute

@Composable
fun EditScreen(
    onBackClick: () -> Unit,
    onSelectLocationClick: (EditRoute.SelectLocation) -> Unit,
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
    onSelectLocationClick: (EditRoute.SelectLocation) -> Unit,
    updatePhotoUri: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        TitleField(title = title, onTitleChange = onTitleChange)

        TagSection(
            tagInput = tagInput,
            onTagInputChange = onTagInputChange,
            onAddTag = onAddTag,
            tags = tags,
            onRemoveTag = onRemoveTag
        )

        DescriptionField(description = description, onDescriptionChange = onDescriptionChange)

        LocationField(w3w = w3w, onSelectLocationClick = onSelectLocationClick)

        ImagePicker(currentUri = photoUri, onImageSelected = updatePhotoUri)
    }
}

@Composable
private fun TitleField(
    title: String,
    onTitleChange: (String) -> Unit
) {
    OutlinedTextField(
        value = title,
        onValueChange = onTitleChange,
        label = { Text("제목", color = PhotoMapTheme.colors.editTag) },
        placeholder = { Text("제목을 입력하세요.", color = PhotoMapTheme.colors.editText.copy(alpha = 0.6f)) },
        modifier = Modifier.fillMaxWidth(),
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
}

@Composable
private fun TagSection(
    tagInput: String,
    onTagInputChange: (String) -> Unit,
    onAddTag: () -> Unit,
    tags: List<String>,
    onRemoveTag: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = tagInput,
                onValueChange = onTagInputChange,
                label = { Text("태그", color = PhotoMapTheme.colors.editTag) },
                placeholder = { Text("태그를 추가하세요.", color = PhotoMapTheme.colors.editText.copy(alpha = 0.6f)) },
                modifier = Modifier.weight(1f),
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
            Button(
                onClick = onAddTag,
                modifier = Modifier.height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PhotoMapTheme.colors.button
                )
            ) {
                Text("추가", color = Color.White)
            }
        }

        if (tags.isNotEmpty()) {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(tags) { tag ->
                    TagChip(tag = tag, onRemove = { onRemoveTag(tag) })
                }
            }
        }
    }
}

@Composable
private fun DescriptionField(
    description: String,
    onDescriptionChange: (String) -> Unit
) {
    OutlinedTextField(
        value = description,
        onValueChange = onDescriptionChange,
        label = { Text("설명", color = PhotoMapTheme.colors.editTag) },
        placeholder = { Text("설명을 입력하세요.", color = PhotoMapTheme.colors.editText.copy(alpha = 0.6f)) },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 120.dp),
        maxLines = 5,
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
}

@Composable
private fun LocationField(
    w3w: String?,
    onSelectLocationClick: (EditRoute.SelectLocation) -> Unit
) {
    val dummyInteraction = remember { MutableInteractionSource() }

    OutlinedTextField(
        value = w3w ?: "위치를 선택해주세요",
        onValueChange = {},
        readOnly = true,
        interactionSource = dummyInteraction,
        label = { Text("위치 (W3W)", color = PhotoMapTheme.colors.editTag) },
        placeholder = { Text("w3w 주소", color = PhotoMapTheme.colors.editText.copy(alpha = 0.6f)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PhotoMapTheme.colors.button,
            unfocusedBorderColor = PhotoMapTheme.colors.editText.copy(alpha = 0.3f),
            focusedTextColor = PhotoMapTheme.colors.editText,
            unfocusedTextColor = PhotoMapTheme.colors.editText.copy(alpha = 0.8f),
            focusedLabelColor = PhotoMapTheme.colors.button,
            unfocusedLabelColor = PhotoMapTheme.colors.editTag,
            disabledTextColor = PhotoMapTheme.colors.editText.copy(alpha = 0.8f),
            disabledBorderColor = PhotoMapTheme.colors.editText.copy(alpha = 0.3f)
        ),
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "지도에서 선택",
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onSelectLocationClick(EditRoute.SelectLocation) },
                tint = PhotoMapTheme.colors.button
            )
        }
    )
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
            containerColor = PhotoMapTheme.colors.popupBackground,
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(
                    "사진 삭제",
                    color = PhotoMapTheme.colors.textTitle,
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Text(
                    "정말로 이 사진을 삭제하시겠습니까?",
                    color = PhotoMapTheme.colors.textTitle
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDeleteClick()
                    }
                ) {
                    Text("삭제", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("취소", color = PhotoMapTheme.colors.button)
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 12.dp)
    ) {
        // 뒤로가기 버튼
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(48.dp)
                .clickable(onClick = onBackClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "뒤로가기",
                modifier = Modifier.size(26.dp),
                tint = PhotoMapTheme.colors.icon
            )
        }

        // 제목
        Text(
            text = if (photoUiModel.id != null) "편집하기" else "추가하기",
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 19.sp),
            color = PhotoMapTheme.colors.textTitle
        )

        // 오른쪽 버튼들
        Row(
            modifier = Modifier.align(Alignment.CenterEnd),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (photoUiModel.id != null) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clickable { showDeleteDialog = true },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "삭제",
                        modifier = Modifier.size(26.dp),
                        tint = PhotoMapTheme.colors.icon
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clickable(onClick = onSaveClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "저장",
                    modifier = Modifier.size(26.dp),
                    tint = PhotoMapTheme.colors.button
                )
            }
        }
    }
}

@Composable
fun TagChip(
    tag: String,
    onRemove: () -> Unit
) {
    val chipShape = RoundedCornerShape(20.dp)
    val chipColor = PhotoMapTheme.colors.button

    Box(
        modifier = Modifier
            .background(chipColor.copy(alpha = 0.15f), chipShape)
            .border(1.dp, chipColor.copy(alpha = 0.3f), chipShape)
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = tag,
                color = PhotoMapTheme.colors.editText,
                style = MaterialTheme.typography.bodyMedium
            )
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "태그 삭제",
                modifier = Modifier
                    .size(18.dp)
                    .clickable { onRemove() },
                tint = PhotoMapTheme.colors.editText.copy(alpha = 0.7f)
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

    val boxShape = RoundedCornerShape(16.dp)
    val bgColor = PhotoMapTheme.colors.photoSelect
    val borderColor = PhotoMapTheme.colors.editText

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .clickable { launcher.launch("image/*") }
            .background(bgColor.copy(alpha = 0.3f), boxShape)
            .border(2.dp, borderColor.copy(alpha = 0.2f), boxShape),
        contentAlignment = Alignment.Center
    ) {
        if (!currentUri.isNullOrBlank()) {
            Image(
                painter = rememberAsyncImagePainter(currentUri),
                contentDescription = "선택한 사진",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
            )
        } else {
            ImagePickerPlaceholder()
        }
    }
}

@Composable
private fun ImagePickerPlaceholder() {
    val iconColor = PhotoMapTheme.colors.button

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(iconColor.copy(alpha = 0.1f), CircleShape)
                .border(2.dp, iconColor.copy(alpha = 0.3f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Face,
                contentDescription = "사진 선택",
                modifier = Modifier.size(36.dp),
                tint = iconColor
            )
        }
        Text(
            text = "사진을 선택해주세요",
            style = MaterialTheme.typography.bodyMedium,
            color = PhotoMapTheme.colors.editText.copy(alpha = 0.6f)
        )
    }
}
