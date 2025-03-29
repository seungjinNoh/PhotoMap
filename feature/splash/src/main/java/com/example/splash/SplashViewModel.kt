package com.example.splash

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val trackingManager: TrackingManager
) : ViewModel() {

    private val _isFirstLaunch = mutableStateOf(!trackingManager.isPermissionRequested())
    val isFirstLaunch: State<Boolean> = _isFirstLaunch

    fun markPermissionRequested() {
        trackingManager.setPermissionRequested(true)
        _isFirstLaunch.value = false
    }

}