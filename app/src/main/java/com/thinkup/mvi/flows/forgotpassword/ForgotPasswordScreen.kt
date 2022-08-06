package com.thinkup.mvi.flows.forgotpassword

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.thinkup.mvi.R
import com.thinkup.mvi.ui.HOME_TAB
import com.thinkup.mvi.ui.shared.TkupButton
import com.thinkup.mvi.ui.shared.TkupButtonConfig
import com.thinkup.mvi.ui.shared.TkupCustomLoader
import com.thinkup.mvi.ui.shared.TkupCustomLoaderConfig
import com.thinkup.mvi.ui.shared.TkupCustomSuccess
import com.thinkup.mvi.ui.shared.TkupCustomSuccessConfig
import com.thinkup.mvi.ui.shared.TkupInput
import com.thinkup.mvi.ui.shared.TkupInputConfig
import com.thinkup.mvi.ui.shared.TkupNavigationTitle
import com.thinkup.mvi.ui.shared.ShowAlertMessage
import com.thinkup.mvi.ui.shared.Type
import com.thinkup.mvi.ui.theme.DIMEN_XX_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_DOUBLE_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_TRIPLE_NORMAL
import com.thinkup.mvi.ui.theme.Typography
import com.thinkup.mvi.ui.theme.screenMessage

@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.state.collectAsState()

    if (state.goBack) navController.popBackStack()

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
                TkupNavigationTitle(
                    title = stringResource(id = R.string.forgot_pass_title)
                ) { navController.navigateUp() }

                Text(
                    text = stringResource(id = R.string.forgot_pass_message),
                    style = Typography.screenMessage,
                    modifier = Modifier.padding(top = DIMEN_DOUBLE_NORMAL, start = DIMEN_DOUBLE_NORMAL, end = DIMEN_DOUBLE_NORMAL, bottom = DIMEN_TRIPLE_NORMAL)
                )

                // Inputs
                TkupInput(
                    modifier = Modifier.padding(top = DIMEN_XX_NORMAL),
                    TkupInputConfig(
                        text = state.email,
                        placeholder = stringResource(id = R.string.login_email_placeholder),
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done,
                        errorMessage = state.isEmailError
                    )
                ) { viewModel.onChangeEmail(it) }
            }
        }
        // Sign-in button
        TkupButton(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(start = DIMEN_XX_NORMAL, end = DIMEN_XX_NORMAL, bottom = DIMEN_XX_NORMAL, top = DIMEN_XX_NORMAL),
            config = TkupButtonConfig(
                text = stringResource(id = R.string.forgot_pass_button),
            )
        ) { viewModel.forgotPassword() }
    }

    if (state.alertMessage?.isSuccess == true) {
        TkupCustomSuccess(
            config = TkupCustomSuccessConfig(
                visible = true,
                title = stringResource(id = R.string.success),
                message = stringResource(id = R.string.forgot_pass_success_message)
            ),
            onHideAlertMessage = { viewModel.onHideAlert() }
        )
    } else {
        ShowAlertMessage(
            alertMessage = state.alertMessage,
            onHideAlertMessage = {
                viewModel.onHideAlert()
            },
            backgroundContent = {}
        )
    }

    TkupCustomLoader(
        TkupCustomLoaderConfig(
            visible = state.showLoading,
            type = Type.SOFT
        )
    )
}