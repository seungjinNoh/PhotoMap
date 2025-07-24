package com.example.designsystem.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier

private val LocalColors = compositionLocalOf<PhotoMapColors> {
    error("No colors")
}

@Composable
fun PhotoMapTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colors: PhotoMapColors = if (darkTheme) {
        PhotoMapColors.defaultDarkColors()
    } else {
        PhotoMapColors.defaultLightColors()
    },
    background: PhotoMapBackground = PhotoMapBackground.defaultBackground(darkTheme),
    content: @Composable () -> Unit,
) {

    CompositionLocalProvider(
        LocalColors provides colors,
        LocalBackgroundTheme provides background
    ) {
        Box(
            modifier = Modifier
                .background(background.color)
        ) {
            content()
        }
    }
}

object PhotoMapTheme {
    val colors: PhotoMapColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current


    val background : PhotoMapBackground
        @Composable
        @ReadOnlyComposable
        get() = LocalBackgroundTheme.current
}