package com.example.home

import android.widget.Space
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.model.photo.PhotoInfo

@Composable
fun HomeScreen(
    onEditClick: (PhotoInfo) -> Unit,
    onAddClick: () -> Unit
) {

    Column {
        Text("Home Screen")
        Button(onClick = {
            val photoInfo = PhotoInfo(
                title = "한강",
                tags = listOf("야경", "한강"),
                description = "25년 봄 한강 야경",
                photoUri = "이미지 경로",
                w3w = "책상/의자/노트북",
                latitude = 36.123,
                longitude = 127.456
            )
            onEditClick(photoInfo)
        }) {
            Text("Edit 화면으로 이동")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            onAddClick()
        }) {
            Text("Edit Add 모드 이동")
        }
    }

}