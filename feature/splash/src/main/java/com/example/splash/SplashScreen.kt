package com.example.splash

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.splash.model.PermissionInfo
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.delay

@Composable
internal fun SplashRoute(navigateHome: () -> Unit) {
    SplashScreen(navigateHome = navigateHome)
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SplashScreen(
    navigateHome: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    Log.d("photomap", "*** 현재 버전: ${Build.VERSION.SDK_INT}")
    val context = LocalContext.current
    val isFirstLaunch by viewModel.isFirstLaunch

    val permissionList = listOf(
        PermissionInfo(
            permission = Manifest.permission.ACCESS_FINE_LOCATION,
            title = "위치 권한",
            description = "현재 위치를 사용하기 위해 필요합니다.",
            isRequired = true,
            imageVector = Icons.Default.Place
        ),
        PermissionInfo(
            permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.READ_MEDIA_IMAGES
            } else {
                Manifest.permission.READ_EXTERNAL_STORAGE
            },
            title = "저장소 권한",
            description = "사진을 불러오기 위해 필요합니다.",
            isRequired = true,
            imageVector = Icons.Default.Share
        )
    )

    val permissionsState = rememberMultiplePermissionsState(
        permissions = permissionList.map { it.permission }
    )

    val allGranted = permissionsState.allPermissionsGranted

    // 이미 전부 허용된 경우 → "PhotoMap" 잠깐 보여주고 홈으로
    LaunchedEffect(allGranted) {
        if (allGranted) {
            delay(1000L)
            navigateHome()
        }
    }

    if (allGranted) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("PhotoMap", style = MaterialTheme.typography.headlineLarge)
        }
    } else {
        SplashPermissionRequestUI(
            permissionList = permissionList,
            permissionsState = permissionsState,
            isFirstLaunch = isFirstLaunch,
            onRequestPermissions = {
                permissionsState.launchMultiplePermissionRequest()
                viewModel.markPermissionRequested()
            },
            onOpenSettings = { openAppSettings(context) }
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SplashPermissionRequestUI(
    permissionList: List<PermissionInfo>,
    permissionsState: MultiplePermissionsState,
    isFirstLaunch: Boolean,
    onRequestPermissions: () -> Unit,
    onOpenSettings: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Spacer(Modifier.height(34.dp))
        Text("권한 상태", style = MaterialTheme.typography.headlineSmall)

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(permissionList) { permissionInfo ->
                val permission = permissionsState.permissions.first {
                    it.permission == permissionInfo.permission
                }

                PermissionItem(
                    permissionInfo = permissionInfo,
                    permissionState = permission,
                    onRequest = {
                        permission.launchPermissionRequest()
                    }
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        when {
            // 앱 처음 시작 -> 바로 권한 팝업 호출
            isFirstLaunch -> {
                LaunchedEffect(Unit) {
                    onRequestPermissions()
                }
            }

            // 2회 이상 거절된 권한 존재 -> 앱 권한 설정 우도
            permissionsState.permissions.any {
                !it.status.isGranted && !it.status.shouldShowRationale
            } -> {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onOpenSettings
                ) {
                    Text("설정으로 이동")
                }
            }

            // 1회 거절된 권한 존재 -> 권한 팝업 호출 버튼 활성화
            permissionsState.permissions.any {
                !it.status.isGranted && it.status.shouldShowRationale
            } -> {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onRequestPermissions
                ) {
                    Text("모든 권한 다시 요청")
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionItem(
    permissionInfo: PermissionInfo,
    permissionState: PermissionState,
    onRequest: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Icon(
            imageVector = permissionInfo.imageVector,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = permissionInfo.title, style = MaterialTheme.typography.titleMedium)
            Text(text = permissionInfo.description, style = MaterialTheme.typography.bodySmall)

            when {
                permissionState.status.isGranted -> {
                    Text("허용됨", color = Color.Green)
                }
                permissionState.status.shouldShowRationale -> {
                    Text("다시 요청 가능", color = Color.Red)
                }
                else -> {
                    Text("영구 거부됨", color = Color.Red)
                }
            }
        }

        if (!permissionState.status.isGranted && permissionState.status.shouldShowRationale) {
            Button(onClick = onRequest) {
                Text("요청")
            }
        }
    }
}

fun openAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.parse("package:${context.packageName}")
        addCategory(Intent.CATEGORY_DEFAULT)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(intent)
}


