package com.thinkup.mvi.flows.editprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.thinkup.mvi.R
import com.thinkup.mvi.ui.SCREEN_RESULT
import com.thinkup.mvi.ui.shared.TkupButton
import com.thinkup.mvi.ui.shared.TkupButtonConfig
import com.thinkup.mvi.ui.shared.TkupCustomLoader
import com.thinkup.mvi.ui.shared.TkupCustomLoaderConfig
import com.thinkup.mvi.ui.shared.TkupInput
import com.thinkup.mvi.ui.shared.TkupInputConfig
import com.thinkup.mvi.ui.shared.TkupNavigationTitle
import com.thinkup.mvi.ui.shared.ShowAlertMessage
import com.thinkup.mvi.ui.shared.Type
import com.thinkup.mvi.ui.theme.Background
import com.thinkup.mvi.ui.theme.DIMEN_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_DOUBLE_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_TRIPLE_NORMAL

@Composable
fun EditProfileScreen(
    viewModel: EditProfileViewModel = hiltViewModel(),
    navController: NavController
) {

    val state by viewModel.state.collectAsState()

    if (state.goBack) {
        navController.previousBackStackEntry
            ?.savedStateHandle
            ?.set(SCREEN_RESULT, true)
        navController.popBackStack()
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Background)
            .padding(horizontal = DIMEN_DOUBLE_NORMAL, vertical = DIMEN_DOUBLE_NORMAL)
    ) {
        val (title, name, username, button) = createRefs()
        TkupNavigationTitle(
            title = stringResource(id = R.string.edit_profile_title),
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
        ) { navController.navigateUp() }

        // Inputs
        TkupInput(
            modifier = Modifier
                .constrainAs(name) {
                    top.linkTo(title.bottom, margin = DIMEN_TRIPLE_NORMAL)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            TkupInputConfig(
                text = state.name.orEmpty(),
                placeholder = stringResource(id = R.string.edit_profile_name_placeholder),
                keyboardType = KeyboardType.Text,
                errorMessage = state.isNameError
            )
        ) { viewModel.onChangeName(it) }
        TkupInput(
            modifier = Modifier
                .constrainAs(username) {
                    top.linkTo(name.bottom, margin = DIMEN_DOUBLE_NORMAL)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            TkupInputConfig(
                text = state.username.orEmpty(),
                placeholder = stringResource(id = R.string.edit_profile_username_placeholder),
                keyboardType = KeyboardType.Text,
                errorMessage = state.isUsernameError,
                imeAction = ImeAction.Done
            )
        ) { viewModel.onChangeUsername(it) }

        TkupButton(
            modifier = Modifier
                .constrainAs(button) {
                    bottom.linkTo(parent.bottom, margin = DIMEN_DOUBLE_NORMAL)
                    start.linkTo(parent.start, margin = DIMEN_DOUBLE_NORMAL)
                    end.linkTo(parent.end, margin = DIMEN_DOUBLE_NORMAL)
                }
                .padding(start = DIMEN_NORMAL, end = DIMEN_NORMAL),
            config = TkupButtonConfig(
                text = stringResource(id = R.string.save),
            )
        ) { viewModel.edit() }
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