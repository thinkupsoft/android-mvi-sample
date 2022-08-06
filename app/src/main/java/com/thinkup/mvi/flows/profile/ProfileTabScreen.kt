package com.thinkup.mvi.flows.profile

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Scale
import com.thinkup.mvi.R
import com.thinkup.mvi.compose.TkupAppState
import com.thinkup.mvi.compose.rememberTkupAppState
import com.thinkup.mvi.ui.CAMERA_IMAGE_SOURCE
import com.thinkup.mvi.ui.GALLERY_IMAGE_SOURCE
import com.thinkup.mvi.ui.HOME_TAB
import com.thinkup.mvi.ui.SCREEN_RESULT
import com.thinkup.mvi.ui.shared.TkupButton
import com.thinkup.mvi.ui.shared.TkupButtonConfig
import com.thinkup.mvi.ui.shared.TkupCustomLoader
import com.thinkup.mvi.ui.shared.TkupCustomLoaderConfig
import com.thinkup.mvi.ui.shared.TkupDialog
import com.thinkup.mvi.ui.shared.TkupImageSource
import com.thinkup.mvi.ui.shared.TkupLogout
import com.thinkup.mvi.ui.shared.ShowAlertMessage
import com.thinkup.mvi.ui.shared.Type
import com.thinkup.mvi.ui.theme.DIMEN_X_SMEDIUM
import com.thinkup.mvi.ui.theme.DIMEN_VERY_BIG_IMAGE
import com.thinkup.mvi.ui.theme.DIMEN_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_MIN_SPACE
import com.thinkup.mvi.ui.theme.DIMEN_XX_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_DOUBLE_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_TRIPLE_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_SMALL_BUTTON
import com.thinkup.mvi.ui.theme.DIMEN_BUTTON
import com.thinkup.mvi.ui.theme.Gray3
import com.thinkup.mvi.ui.theme.PrimaryCyan
import com.thinkup.mvi.ui.theme.PrimaryRed
import com.thinkup.mvi.ui.theme.Typography
import com.thinkup.mvi.ui.theme.darkTitle
import com.thinkup.mvi.ui.theme.linkButton
import com.thinkup.mvi.ui.theme.smallData
import com.thinkup.mvi.ui.theme.smallHeader
import com.thinkup.mvi.utils.launchCameraPicker
import com.thinkup.mvi.utils.launchImagePicker
import com.thinkup.common.ui.DateUtils
import com.thinkup.common.whitespace
import kotlinx.coroutines.launch

@Composable
fun ProfileTabScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    navController: NavController,
    onEditProfileClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    onChangeClicked: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    val screenResult = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<Boolean>(SCREEN_RESULT)
    screenResult?.value?.let {
        if (it) {
            navController.currentBackStackEntry?.savedStateHandle?.set(SCREEN_RESULT, false)
            viewModel.load()
        }
    }

    val galleryUriImage = remember { mutableStateOf<Uri?>(null) }
    val cameraBitmapImage = remember { mutableStateOf<Bitmap?>(null) }

    val openLogoutDialog = remember { mutableStateOf(false) }
    val openSourceDialog = remember { mutableStateOf(false) }
    val openCameraPreview = remember { mutableStateOf(false) }
    val openGalleryPreview = remember { mutableStateOf(false) }
    val dateUtils = DateUtils()
    val lastLoginString = state.user?.let {
        val lastLoginDate = dateUtils.parse(state.user?.lastLoginDate.orEmpty(), DateUtils.FORMAT_DATE7)
        dateUtils.format(lastLoginDate, DateUtils.FORMAT_DATE3)
    }

    gotoMainTab(state, navController)
    onOpenLogoutDialog(openLogoutDialog, viewModel)
    onOpenSourceDialog(openSourceDialog, openCameraPreview, openGalleryPreview)
    openCameraPreview(viewModel, cameraBitmapImage, galleryUriImage, openSourceDialog, openCameraPreview)
    openPickerImageDialog(viewModel, galleryUriImage, cameraBitmapImage, openSourceDialog, openGalleryPreview)

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(modifier = Modifier.padding(top = DIMEN_BUTTON))
            // user avatar
            Box {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(
                            cameraBitmapImage.value ?: galleryUriImage.value ?: state.user?.avatar.orEmpty()
                        )
                        .decoderFactory(SvgDecoder.Factory())
                        .scale(Scale.FIT)
                        .crossfade(true)
                        .build(),
                    contentDescription = "user avatar",
                    placeholder = painterResource(id = R.drawable.tkup_default_avatar),
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.tkup_default_avatar),
                    fallback = painterResource(id = R.drawable.tkup_default_avatar),
                    modifier = Modifier
                        .clip(CircleShape)
                        .align(Alignment.Center)
                        .size(DIMEN_VERY_BIG_IMAGE)
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(DIMEN_TRIPLE_NORMAL)
                        .background(PrimaryCyan, shape = CircleShape)
                        .border(DIMEN_MIN_SPACE, color = Gray3, shape = CircleShape)
                        .clip(CircleShape)
                        .clickable { openSourceDialog.value = true }
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.tkup_camera_edit),
                        contentDescription = "edit avatar",
                        alignment = Alignment.Center,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            Spacer(modifier = Modifier.padding(top = DIMEN_DOUBLE_NORMAL))
        }
        // User name & edit action
        item {
            Row(modifier = Modifier
                .clickable { onEditProfileClicked() }
            ) {
                Text(
                    modifier = Modifier,
                    text = state.user?.name.orEmpty(),
                    style = Typography.darkTitle
                )
                Spacer(modifier = Modifier.padding(start = DIMEN_X_SMEDIUM))
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.tkup_pen_edit),
                    contentDescription = "edit user",
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
            Spacer(modifier = Modifier.padding(top = DIMEN_TRIPLE_NORMAL))
        }
        // User info card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = DIMEN_DOUBLE_NORMAL)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(DIMEN_XX_NORMAL)
                    )
                    .border(
                        width = DIMEN_MIN_SPACE,
                        color = PrimaryCyan,
                        shape = RoundedCornerShape(DIMEN_XX_NORMAL)
                    )
            ) {
                Column(modifier = Modifier.padding(horizontal = DIMEN_XX_NORMAL)) {
                    Spacer(modifier = Modifier.padding(top = DIMEN_XX_NORMAL))
                    Text(text = stringResource(id = R.string.profile_email), style = Typography.smallHeader)
                    Text(text = state.user?.email.orEmpty(), style = Typography.smallData)
                    Spacer(modifier = Modifier.padding(top = DIMEN_XX_NORMAL))
                    Text(text = stringResource(id = R.string.profile_username), style = Typography.smallHeader)
                    Text(text = state.user?.username.orEmpty(), style = Typography.smallData)
                    Spacer(modifier = Modifier.padding(top = DIMEN_XX_NORMAL))
                    Text(text = stringResource(id = R.string.profile_last_login), style = Typography.smallHeader)
                    Text(text = lastLoginString.orEmpty(), style = Typography.smallData)
                    Spacer(modifier = Modifier.padding(top = DIMEN_XX_NORMAL))
                }
            }
            Spacer(modifier = Modifier.padding(top = DIMEN_DOUBLE_NORMAL))
        }
        // Change password button
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = DIMEN_DOUBLE_NORMAL)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(DIMEN_XX_NORMAL)
                    )
                    .border(
                        width = DIMEN_MIN_SPACE,
                        color = PrimaryCyan,
                        shape = RoundedCornerShape(DIMEN_XX_NORMAL)
                    )
                    .height(DIMEN_SMALL_BUTTON)
                    .clickable { onChangeClicked() }
            ) {
                Text(
                    text = stringResource(id = R.string.profile_change_password),
                    style = Typography.smallHeader,
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.padding(top = DIMEN_BUTTON))
        }
        // Logout button
        item {
            TkupButton(
                modifier = Modifier
                    .padding(horizontal = DIMEN_DOUBLE_NORMAL),
                config = TkupButtonConfig(
                    text = stringResource(id = R.string.logout)
                ),
                onClick = { openLogoutDialog.value = true }
            )
            Spacer(modifier = Modifier.padding(top = DIMEN_NORMAL))
        }
        // Delete button
        item {
            val annotatedString = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = PrimaryRed,
                    )
                ) { append("${String.whitespace()} ${stringResource(id = R.string.profile_delete_account)}") }
            }
            ClickableText(text = annotatedString, style = Typography.linkButton) {
                onDeleteClicked()
            }
            Spacer(modifier = Modifier.padding(top = DIMEN_NORMAL))
        }
    }

    ShowAlertMessage(
        alertMessage = state.alertMessage,
        onHideAlertMessage = {
            viewModel.onHideAlert()
        },
        backgroundContent = { }
    )

    TkupCustomLoader(
        TkupCustomLoaderConfig(
            visible = state.showLoading,
            type = Type.SOFT
        )
    )
}

fun gotoMainTab(state: ProfileScreenState, navController: NavController) {
    // go back on login success
    if (state.logoutCompleted) navController.popBackStack(HOME_TAB, false)
}

@Composable
fun onOpenLogoutDialog(openLogoutDialog: MutableState<Boolean>, viewModel: ProfileViewModel) {
    // open dialog logout
    if (openLogoutDialog.value) TkupDialog(onDismiss = { openLogoutDialog.value = false }) {
        TkupLogout {
            openLogoutDialog.value = false
            viewModel.logout()
        }
    }
}

@Composable
fun onOpenSourceDialog(
    openSourceDialog: MutableState<Boolean>,
    openCameraPreview: MutableState<Boolean>,
    openGalleryPreview: MutableState<Boolean>
) {
    // open dialog image source
    if (openSourceDialog.value) TkupDialog(onDismiss = { openSourceDialog.value = false }) {
        TkupImageSource {
            when (it) {
                GALLERY_IMAGE_SOURCE -> {
                    openGalleryPreview.value = true
                }
                CAMERA_IMAGE_SOURCE -> {
                    openCameraPreview.value = true
                }
            }
        }
    }
}

@Composable
fun openPickerImageDialog(
    viewModel: ProfileViewModel,
    value: MutableState<Uri?>,
    valueAux: MutableState<Bitmap?>,
    openSourceDialog: MutableState<Boolean>,
    openGalleryPreview: MutableState<Boolean>,
    appState: TkupAppState = rememberTkupAppState(),
) {
    if (openGalleryPreview.value) {
        launchImagePicker {
            openGalleryPreview.value = false
            openSourceDialog.value = false
            it?.let {
                appState.coroutineScope.launch {
                    value.value = null
                    valueAux.value = null
                    viewModel.updateAvatar(uri = it)
                    value.value = it
                }
            }
        }
    }
}

@Composable
fun openCameraPreview(
    viewModel: ProfileViewModel,
    value: MutableState<Bitmap?>,
    valueAux: MutableState<Uri?>,
    openSourceDialog: MutableState<Boolean>,
    openCameraPreview: MutableState<Boolean>,
    appState: TkupAppState = rememberTkupAppState(),
) {
    if (openCameraPreview.value) {
        launchCameraPicker {
            openCameraPreview.value = false
            openSourceDialog.value = false
            it?.let {
                appState.coroutineScope.launch {
                    value.value = null
                    valueAux.value = null
                    viewModel.updateAvatar(bitmap = it)
                    value.value = it
                }
            }
        }
    }
}