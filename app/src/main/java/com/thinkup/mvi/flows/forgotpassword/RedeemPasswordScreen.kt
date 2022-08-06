package com.thinkup.mvi.flows.forgotpassword

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.thinkup.mvi.R
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
import com.thinkup.mvi.ui.theme.DIMEN_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_XX_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_DOUBLE_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_TRIPLE_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_X_SMALL
import com.thinkup.mvi.ui.theme.Typography
import com.thinkup.mvi.ui.theme.screenMessage

@Composable
fun RedeemPasswordScreen(
    viewModel: RedeemPasswordViewModel = hiltViewModel(),
    navController: NavController,
    token: String
) {
    val state by viewModel.state.collectAsState()

    if (state.goBack) navController.popBackStack()

    Column(modifier = Modifier.fillMaxSize()) {

        TkupNavigationTitle(
            title = stringResource(id = R.string.redeem_pass_title)
        ) { navController.navigateUp() }

        Column(
            modifier = Modifier
                .weight(1f)
        ) {

            Text(
                text = stringResource(id = R.string.redeem_pass_message),
                style = Typography.screenMessage,
                modifier = Modifier.padding(top = DIMEN_DOUBLE_NORMAL, start = DIMEN_DOUBLE_NORMAL, end = DIMEN_DOUBLE_NORMAL, bottom = DIMEN_TRIPLE_NORMAL)
            )

            // Inputs
            TkupInput(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = DIMEN_NORMAL)
                    .padding(top = DIMEN_X_SMALL),
                TkupInputConfig(
                    text = state.newPassword.orEmpty(),
                    placeholder = stringResource(id = R.string.change_password_new),
                    keyboardType = KeyboardType.Password,
                    errorMessage = state.isPasswordError,
                    isPasswordField = true
                )
            ) { viewModel.onChangeNew(it) }

            // Inputs
            TkupInput(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = DIMEN_NORMAL)
                    .padding(top = DIMEN_X_SMALL),
                TkupInputConfig(
                    text = state.repeatPassword.orEmpty(),
                    placeholder = stringResource(id = R.string.change_password_repeat),
                    keyboardType = KeyboardType.Password,
                    errorMessage = state.isRepeatError,
                    isPasswordField = true,
                    imeAction = ImeAction.Done
                )
            ) { viewModel.onChangeRepeat(it) }
        }
        // Change button
        TkupButton(
            modifier = Modifier
                .padding(all = DIMEN_XX_NORMAL),
            config = TkupButtonConfig(
                text = stringResource(id = R.string.change_password),
            )
        ) { viewModel.redeemPassword(token) }
    }

    if (state.alertMessage?.isSuccess == true) {
        TkupCustomSuccess(
            config = TkupCustomSuccessConfig(
                visible = true,
                title = stringResource(id = R.string.success),
                message = stringResource(id = R.string.redeem_pass_success_message)
            ),
            onHideAlertMessage = { viewModel.onHideAlert() }
        )
    } else {
        ShowAlertMessage(
            alertMessage = state.alertMessage,
            onHideAlertMessage = {
                viewModel.onHideAlert()
            },
            backgroundContent = { }
        )
    }

    TkupCustomLoader(
        TkupCustomLoaderConfig(
            visible = state.showLoading,
            type = Type.SOFT
        )
    )
}