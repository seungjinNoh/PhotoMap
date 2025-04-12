package com.example.photomap

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.photomap.navigation.MainNavHost
import com.example.photomap.navigation.rememberMainNavigator
import kotlinx.collections.immutable.toPersistentList

@Composable
fun PhotoMapMain() {
    val mainNavigator = rememberMainNavigator()
    Scaffold(
        modifier = Modifier,
        content = { padding ->
            MainNavHost(
                mainNavigator = mainNavigator,
                padding = padding
            )
        },
        bottomBar = {
            MainBottomBar(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(start = 8.dp, end = 8.dp, bottom = 28.dp),
                visible = mainNavigator.shouldShowBottomBar(),
                tabs = MainTab.entries.toPersistentList(),
                currentTab = mainNavigator.currentTab,
                onTabSelected = { mainNavigator.navigate(it) }
            )
        }
    )

}