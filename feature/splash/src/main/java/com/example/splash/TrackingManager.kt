package com.example.splash

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TrackingManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val prefs = context.getSharedPreferences("app_tracking", Context.MODE_PRIVATE)

    fun isPermissionRequested(): Boolean {
        return prefs.getBoolean("permission_requested", false)
    }

    fun setPermissionRequested(isRequested: Boolean) {
        prefs.edit().putBoolean("permission_requested", isRequested).apply()
    }

}