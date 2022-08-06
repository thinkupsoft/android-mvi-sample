package com.thinkup.mvi.flows.register

import android.Manifest
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.thinkup.mvi.R
import com.thinkup.mvi.ui.HOME_TAB
import com.thinkup.mvi.ui.shared.ShowAlertMessage
import com.thinkup.mvi.ui.shared.TkupButton
import com.thinkup.mvi.ui.shared.TkupButtonConfig
import com.thinkup.mvi.ui.shared.TkupCustomLoader
import com.thinkup.mvi.ui.shared.TkupCustomLoaderConfig
import com.thinkup.mvi.ui.shared.TkupInput
import com.thinkup.mvi.ui.shared.TkupInputConfig
import com.thinkup.mvi.ui.shared.Type
import com.thinkup.mvi.ui.theme.DIMEN_XX_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_DOUBLE_NORMAL
import com.thinkup.mvi.ui.theme.Error
import com.thinkup.mvi.ui.theme.Success
import com.thinkup.mvi.utils.getPermissions

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    navController: NavController
) {

    val state by viewModel.state.collectAsState()

    val launcher = getPermissions(listOf(Manifest.permission.POST_NOTIFICATIONS)) {
        navController.popBackStack()
    }

    if (state.goBack) SideEffect {
        launcher.launchMultiplePermissionRequest()
    }

    BackHandler {
        navController.popBackStack(HOME_TAB, false)
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        LazyColumn(
            Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .padding(start = DIMEN_XX_NORMAL, end = DIMEN_XX_NORMAL, top = DIMEN_DOUBLE_NORMAL)
                .weight(1f)
        ) {

            item {
                // Logo
                Image(
                    modifier = Modifier
                        .padding(top = DIMEN_XX_NORMAL)
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(),
                    bitmap = ImageBitmap.imageResource(id = R.drawable.tkup_logo),
                    contentDescription = "logo",
                    alignment = Alignment.TopCenter
                )

                // Inputs
                TkupInput(
                    modifier = Modifier.padding(top = DIMEN_XX_NORMAL),
                    TkupInputConfig(
                        text = state.name.orEmpty(),
                        placeholder = stringResource(id = R.string.register_name_placeholder),
                        keyboardType = KeyboardType.Text,
                        errorMessage = state.isNameError
                    )
                ) { viewModel.onChangeName(it) }
                TkupInput(
                    modifier = Modifier.padding(top = DIMEN_XX_NORMAL),
                    TkupInputConfig(
                        text = state.email.orEmpty(),
                        placeholder = stringResource(id = R.string.register_email_placeholder),
                        keyboardType = KeyboardType.Email,
                        errorMessage = state.isEmailError,
                        trailingIcon = state.isEmailInUse?.let { if (it) R.drawable.tkup_error_small_icon else R.drawable.tkup_success_small_icon }
                            ?: run { null },
                        trailingIconColor = state.isEmailInUse?.let { if (it) Error else Success } ?: run { null }
                    )
                ) { viewModel.onChangeEmail(it) }
                TkupInput(
                    modifier = Modifier.padding(top = DIMEN_XX_NORMAL),
                    TkupInputConfig(
                        text = state.password.orEmpty(),
                        placeholder = stringResource(id = R.string.register_password_placeholder),
                        keyboardType = KeyboardType.Password,
                        isPasswordField = true,
                        errorMessage = state.isPasswordError
                    )
                ) { viewModel.onChangePassword(it) }
                TkupInput(
                    modifier = Modifier.padding(top = DIMEN_XX_NORMAL),
                    TkupInputConfig(
                        text = state.repeatPassword.orEmpty(),
                        placeholder = stringResource(id = R.string.register_repeat_password_placeholder),
                        keyboardType = KeyboardType.Password,
                        isPasswordField = true,
                        errorMessage = state.isRepeatPasswordError,
                        imeAction = ImeAction.Done
                    )
                ) { viewModel.onChangeRepeatPassword(it) }
            }
        }
        // Sign-in button
        TkupButton(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(start = DIMEN_XX_NORMAL, end = DIMEN_XX_NORMAL, bottom = DIMEN_XX_NORMAL, top = DIMEN_XX_NORMAL),
            config = TkupButtonConfig(
                text = stringResource(id = R.string.register_uppercase),
            )
        ) { viewModel.register() }
    }

    ShowAlertMessage(
        alertMessage = state.alertMessage,
        onHideAlertMessage = {
            viewModel.onHideAlert()
        },
        backgroundContent = {}
    )

    TkupCustomLoader(
        TkupCustomLoaderConfig(
            visible = state.showLoading,
            type = Type.SOFT
        )
    )
}