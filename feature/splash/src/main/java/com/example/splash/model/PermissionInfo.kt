package com.example.splash.model

import androidx.compose.ui.graphics.vector.ImageVector

data class PermissionInfo(
    val permission: String,
    val title: String,
    val description: String,
    val isRequired: Boolean,
    val imageVector: ImageVector
)
