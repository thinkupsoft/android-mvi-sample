package com.thinkup.mvi.ui.shared

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.thinkup.mvi.R
import com.thinkup.mvi.ui.CAMERA_IMAGE_SOURCE
import com.thinkup.mvi.ui.GALLERY_IMAGE_SOURCE
import com.thinkup.mvi.ui.theme.DIMEN_X_SMEDIUM
import com.thinkup.mvi.ui.theme.DIMEN_MIN_SPACE
import com.thinkup.mvi.ui.theme.DIMEN_XX_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_DOUBLE_NORMAL
import com.thinkup.mvi.ui.theme.PrimaryCyan
import com.thinkup.mvi.ui.theme.Typography
import com.thinkup.mvi.ui.theme.dialogMessage
import com.thinkup.mvi.utils.getPermissions

@Composable
fun TkupDialog(
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(dismissOnBackPress = true)
    ) {
        content()
    }
}

@Composable
fun TkupLogout(onLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .background(Color.White, shape = RoundedCornerShape(DIMEN_XX_NORMAL))
            .border(DIMEN_MIN_SPACE, color = PrimaryCyan, shape = RoundedCornerShape(DIMEN_XX_NORMAL))
            .padding(DIMEN_DOUBLE_NORMAL)
    ) {
        Text(
            style = Typography.dialogMessage,
            textAlign = TextAlign.Center,
            text = stringResource(id = R.string.profile_logout_msg),
            modifier = Modifier
                .padding(bottom = DIMEN_DOUBLE_NORMAL)
        )
        TkupButton(
            modifier = Modifier,
            config = TkupButtonConfig(text = stringResource(id = R.string.logout)),
            onClick = onLogout
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TkupImageSource(onSelect: (Int) -> Unit) {
    Column(
        modifier = Modifier
            .background(Color.White, shape = RoundedCornerShape(DIMEN_XX_NORMAL))
            .border(DIMEN_MIN_SPACE, color = PrimaryCyan, shape = RoundedCornerShape(DIMEN_XX_NORMAL))
            .padding(DIMEN_DOUBLE_NORMAL)
    ) {
        val permissionsRequest = getPermissions(permissions = listOf(Manifest.permission.CAMERA), onResult = {
            if (it) onSelect(CAMERA_IMAGE_SOURCE)
        })
        TkupButton(
            modifier = Modifier.padding(bottom = DIMEN_X_SMEDIUM),
            config = TkupButtonConfig(
                text = stringResource(id = R.string.camera),
                icon = R.drawable.tkup_camera_icon
            ),
            onClick = {
                permissionsRequest.launchMultiplePermissionRequest()
            }
        )
        TkupButton(
            modifier = Modifier,
            config = TkupButtonConfig(
                text = stringResource(id = R.string.gallery),
                icon = R.drawable.tkup_gallery_icon
            ),
            onClick = { onSelect(GALLERY_IMAGE_SOURCE) }
        )
    }
}