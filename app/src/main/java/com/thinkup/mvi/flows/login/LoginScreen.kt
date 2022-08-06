package com.thinkup.mvi.flows.login

import android.Manifest
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
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
import com.thinkup.mvi.ui.theme.PrimaryRed
import com.thinkup.mvi.ui.theme.Typography
import com.thinkup.mvi.ui.theme.smallLink
import com.thinkup.mvi.utils.getPermissions

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    navController: NavController,
    onForgotPasswordClicked: () -> Unit
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
                    bitmap = ImageBitmap.imageResource(id = R.drawable.tkup_logo),
                    contentDescription = "logo",
                    alignment = Alignment.TopCenter,
                    modifier = Modifier
                        .padding(top = DIMEN_XX_NORMAL)
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth()
                )

                // Inputs
                TkupInput(
                    modifier = Modifier.padding(top = DIMEN_XX_NORMAL),
                    TkupInputConfig(
                        text = state.email.orEmpty(),
                        placeholder = stringResource(id = R.string.login_email_placeholder),
                        keyboardType = KeyboardType.Email,
                        errorMessage = state.isEmailError
                    )
                ) { viewModel.onChangeEmail(it) }
                TkupInput(
                    modifier = Modifier.padding(top = DIMEN_XX_NORMAL),
                    TkupInputConfig(
                        text = state.password.orEmpty(),
                        placeholder = stringResource(id = R.string.login_password_placeholder),
                        keyboardType = KeyboardType.Password,
                        isPasswordField = true,
                        errorMessage = state.isPasswordError,
                        imeAction = ImeAction.Done
                    )
                ) { viewModel.onChangePassword(it) }

                // Forgot link
                val annotatedString = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = PrimaryRed,
                        )
                    ) { append(stringResource(id = R.string.login_forgot_pass)) }
                }
                ClickableText(
                    text = annotatedString,
                    style = Typography.smallLink,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = DIMEN_XX_NORMAL, end = DIMEN_DOUBLE_NORMAL)
                        .align(Alignment.End)
                ) { onForgotPasswordClicked() }
            }
        }
        // Sign-in button
        TkupButton(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(start = DIMEN_XX_NORMAL, end = DIMEN_XX_NORMAL, bottom = DIMEN_XX_NORMAL, top = DIMEN_XX_NORMAL),
            config = TkupButtonConfig(
                text = stringResource(id = R.string.sign_in_uppercase),
            )
        ) { viewModel.login() }
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