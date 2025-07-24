package com.example.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.example.designsystem.R

@Immutable
public data class PhotoMapColors(
    val background: Color,
    val bottomIcon: Color,
    val bottomIconDark: Color,
    val bottomIconSelected: Color,
    val icon: Color,
    val textTitle: Color,
    val editTag: Color,
    val editText: Color,
    val editTextSelected: Color,
    val button: Color,
    val photoSelect: Color,
    val searchTagText: Color,
) {

    companion object {

        @Composable
        fun defaultLightColors(): PhotoMapColors = PhotoMapColors(
            background = colorResource(id = R.color.photo_background),
            bottomIcon = colorResource(id = R.color.photo_bottom_icon),
            bottomIconDark = colorResource(id = R.color.photo_bottom_icon_dark),
            bottomIconSelected = colorResource(id = R.color.photo_bottom_icon_selected),
            icon = colorResource(id = R.color.photo_icon),
            textTitle = colorResource(id = R.color.photo_text_title),
            editTag = colorResource(id = R.color.photo_edit_tag),
            editText = colorResource(id = R.color.photo_edit_text),
            editTextSelected = colorResource(id = R.color.photo_edit_text_selected),
            button = colorResource(id = R.color.photo_button),
            photoSelect = colorResource(id = R.color.photo_select),
            searchTagText = colorResource(id = R.color.photo_search_tag_text)
        )

        @Composable
        fun defaultDarkColors(): PhotoMapColors = PhotoMapColors(
            background = colorResource(id = R.color.photo_background_dark),
            bottomIcon = colorResource(id = R.color.photo_bottom_icon_dark),
            bottomIconDark = colorResource(id = R.color.photo_bottom_icon_dark), // same as light
            bottomIconSelected = colorResource(id = R.color.photo_bottom_icon_selected),
            icon = colorResource(id = R.color.photo_icon),
            textTitle = Color.White, // override with white for contrast
            editTag = colorResource(id = R.color.photo_edit_tag),
            editText = Color.LightGray,
            editTextSelected = colorResource(id = R.color.photo_edit_text_selected),
            button = colorResource(id = R.color.photo_button_dark),
            photoSelect = colorResource(id = R.color.photo_select_dark),
            searchTagText = Color.Gray
        )
    }
}