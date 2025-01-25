package com.example.expensetracker.util

import android.Manifest
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestNotificationPermission(
    onPermissionResult: (Boolean) -> Unit
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val permissionState = rememberPermissionState(
            Manifest.permission.POST_NOTIFICATIONS
        ) { isGranted ->
            onPermissionResult(isGranted)
        }

        LaunchedEffect(Unit) {
            if (!permissionState.status.isGranted &&
                !permissionState.status.shouldShowRationale
            ) {
                permissionState.launchPermissionRequest()
            }
        }
    } else {
        onPermissionResult(true)
    }
} 