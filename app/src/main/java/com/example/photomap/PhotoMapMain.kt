package com.example.photomap

import androidx.compose.runtime.Composable
import com.example.photomap.navigation.MainNavHost
import com.example.photomap.navigation.rememberMainNavigator

@Composable
fun PhotoMapMain() {
    val mainNavigator = rememberMainNavigator()
    MainNavHost(mainNavigator = mainNavigator)
}