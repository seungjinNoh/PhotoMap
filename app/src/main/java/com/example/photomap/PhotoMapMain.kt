package com.example.photomap

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.designsystem.theme.PhotoMapTheme
import com.example.photomap.navigation.MainNavHost
import com.example.photomap.navigation.rememberMainNavigator

@Composable
fun PhotoMapMain() {

    val context = LocalContext.current
    val mainNavigator = rememberMainNavigator()
    var lastBackPressTime by remember { mutableStateOf(0L) }

    BackHandler {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastBackPressTime < 2000) {
            // 2초 이내 두 번 누름 → 앱 종료
            (context as? Activity)?.finish()
        } else {
            lastBackPressTime = currentTime
            Toast.makeText(context, "한 번 더 누르면 종료됩니다", Toast.LENGTH_SHORT).show()
        }
    }

    PhotoMapTheme {
        Scaffold(
            modifier = Modifier.background(PhotoMapTheme.colors.background)
        ) { _ ->
            Box {
                MainNavHost(
                    mainNavigator = mainNavigator
                )
            }
        }
    }
}