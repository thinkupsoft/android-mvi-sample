package com.thinkup.mvi.flows.deleteaccount

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.thinkup.mvi.R
import com.thinkup.mvi.ui.HOME_TAB
import com.thinkup.mvi.ui.shared.TkupButton
import com.thinkup.mvi.ui.shared.TkupButtonConfig
import com.thinkup.mvi.ui.shared.TkupCustomLoader
import com.thinkup.mvi.ui.shared.TkupCustomLoaderConfig
import com.thinkup.mvi.ui.shared.TkupNavigationTitle
import com.thinkup.mvi.ui.shared.ShowAlertMessage
import com.thinkup.mvi.ui.shared.Type
import com.thinkup.mvi.ui.theme.Background
import com.thinkup.mvi.ui.theme.DIMEN_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_DOUBLE_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_TRIPLE_NORMAL
import com.thinkup.mvi.ui.theme.Error
import com.thinkup.mvi.ui.theme.Typography
import com.thinkup.mvi.ui.theme.screenMessage

@Composable
fun DeleteAccountScreen(
    viewModel: DeleteAccountViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.state.collectAsState()

    if (state.goBack) navController.popBackStack(HOME_TAB, false)

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Background)
    ) {
        val (title, message, button) = createRefs()
        TkupNavigationTitle(
            title = stringResource(id = R.string.delete_account_title),
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
        ) { navController.navigateUp() }

        Text(
            text = stringResource(id = R.string.delete_account_message),
            style = Typography.screenMessage,
            modifier = Modifier
                .constrainAs(message) {
                    top.linkTo(title.bottom, margin = DIMEN_TRIPLE_NORMAL)
                    start.linkTo(parent.start, margin = DIMEN_DOUBLE_NORMAL)
                    end.linkTo(parent.end, margin = DIMEN_DOUBLE_NORMAL)
                }
                .padding(start = DIMEN_DOUBLE_NORMAL, end = DIMEN_DOUBLE_NORMAL)
        )

        TkupButton(
            modifier = Modifier
                .constrainAs(button) {
                    bottom.linkTo(parent.bottom, margin = DIMEN_DOUBLE_NORMAL)
                    start.linkTo(parent.start, margin = DIMEN_DOUBLE_NORMAL)
                    end.linkTo(parent.end, margin = DIMEN_DOUBLE_NORMAL)
                }
                .padding(start = DIMEN_NORMAL, end = DIMEN_NORMAL),
            config = TkupButtonConfig(
                text = stringResource(id = R.string.delete_account_button),
                background = Error
            )
        ) { viewModel.delete() }
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