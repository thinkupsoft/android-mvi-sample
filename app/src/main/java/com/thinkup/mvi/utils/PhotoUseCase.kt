package com.thinkup.mvi.utils

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.thinkup.mvi.ui.PHOTO_PICKER_TYPE

@Composable
fun launchImagePicker(onResult: (Uri?) -> Unit) {
    val photoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        onResult(it)
    }
    SideEffect {
        photoLauncher.launch(PHOTO_PICKER_TYPE)
    }
}

@Composable
fun launchCameraPicker(onResult: (Bitmap?) -> Unit) {
    val photoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) {
        onResult(it)
    }
    SideEffect {
        photoLauncher.launch()
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun getPermissions(permissions: List<String>, onResult: (Boolean) -> Unit): MultiplePermissionsState {
    return rememberMultiplePermissionsState(
        permissions = permissions
    ) { permissionStateMap ->
        onResult(!permissionStateMap.containsValue(false))
    }
}