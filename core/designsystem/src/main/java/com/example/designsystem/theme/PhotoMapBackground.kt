package com.example.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.designsystem.R

@Immutable
data class PhotoMapBackground(
    val color: Color = Color.Unspecified,
    val tonalElevation: Dp = Dp.Unspecified
) {

    companion object {
        @Composable
        fun defaultBackground(darkTheme: Boolean): PhotoMapBackground {
            return if (darkTheme) {
                PhotoMapBackground(
                    color = colorResource(id = R.color.photo_background_dark),
                    tonalElevation = 0.dp
                )
            } else {
                PhotoMapBackground(
                    color = colorResource(id = R.color.photo_background),
                    tonalElevation = 0.dp
                )
            }
        }
    }
}

val LocalBackgroundTheme: ProvidableCompositionLocal<PhotoMapBackground> =
    staticCompositionLocalOf { PhotoMapBackground() }