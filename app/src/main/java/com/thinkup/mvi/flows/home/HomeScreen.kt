package com.thinkup.mvi.flows.home

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.thinkup.mvi.R
import com.thinkup.mvi.compose.Flows
import com.thinkup.mvi.compose.NavArg
import com.thinkup.mvi.exception.GracefulCrashHandler
import com.thinkup.mvi.flows.moviedetail.MovieDetailScreen
import com.thinkup.mvi.flows.movies.MovieTabScreen
import com.thinkup.mvi.flows.changepassword.ChangePasswordScreen
import com.thinkup.mvi.flows.deleteaccount.DeleteAccountScreen
import com.thinkup.mvi.flows.editprofile.EditProfileScreen
import com.thinkup.mvi.flows.forgotpassword.ForgotPasswordScreen
import com.thinkup.mvi.flows.forgotpassword.RedeemPasswordScreen
import com.thinkup.mvi.flows.login.LoginScreen
import com.thinkup.mvi.flows.newmovie.NewMovieScreen
import com.thinkup.mvi.flows.notifications.NotificationsTabScreen
import com.thinkup.mvi.flows.profile.ProfileTabScreen
import com.thinkup.mvi.flows.register.RegisterScreen
import com.thinkup.mvi.ui.MOVIES_TAB
import com.thinkup.mvi.ui.HOME_TAB
import com.thinkup.mvi.ui.NOTIFICATIONS_TAB
import com.thinkup.mvi.ui.PROFILE_TAB
import com.thinkup.mvi.ui.shared.AlertMessage
import com.thinkup.mvi.ui.shared.BottomBar
import com.thinkup.mvi.ui.shared.BottomBarDestination
import com.thinkup.mvi.ui.shared.BottomBarItem
import com.thinkup.mvi.ui.shared.IndicatorConfig
import com.thinkup.mvi.ui.shared.NavigationGraph
import com.thinkup.mvi.ui.shared.ShowAlertMessage
import com.thinkup.mvi.ui.theme.Background
import com.thinkup.common.GsonUtil
import com.thinkup.common.getActivity
import com.thinkup.models.app.Movie
import com.thinkup.mvi.ui.theme.PrimaryCyan

@Composable
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    WindowCompat.setDecorFitsSystemWindows((LocalContext.current as ComponentActivity).window, true)
    val systemUiController: SystemUiController = rememberSystemUiController()
    val navController = rememberNavController()
    val isSystemInDarkTheme = isSystemInDarkTheme()
    SideEffect {
        systemUiController.isNavigationBarVisible = true
        systemUiController.setStatusBarColor(Color.White, isSystemInDarkTheme)
    }

    val state by viewModel.state.collectAsState()
    val showSessionExpiredDialog = remember { mutableStateOf(false) }
    val hasSessionExpired = LocalContext.current.getActivity()?.intent?.getBooleanExtra(GracefulCrashHandler.EXTRA_SESSION_EXPIRED, false) ?: false

    val items = listOf(
        BottomBarItem(
            title = stringResource(id = R.string.tab_home_name),
            icon = R.drawable.tkup_home_bar,
            route = HOME_TAB,
            destinations = listOf(
                BottomBarDestination(Flows.LOGIN.route) {
                    LoginScreen(navController = navController, onForgotPasswordClicked = {
                        navController.navigate(Flows.FORGOT_PASSWORD.route)
                    })
                },
                BottomBarDestination(Flows.REGISTER.route) { RegisterScreen(navController = navController) },
                BottomBarDestination(Flows.FORGOT_PASSWORD.route) { ForgotPasswordScreen(navController = navController) },
                BottomBarDestination(Flows.REDEEM_PASSWORD.route, Flows.REDEEM_PASSWORD.navArgs, Flows.REDEEM_PASSWORD.deepLinks) {
                    RedeemPasswordScreen(
                        navController = navController,
                        token = navController.currentBackStackEntry?.arguments?.getString(NavArg.Token.key).orEmpty()
                    )
                },
                BottomBarDestination(Flows.NEW_MOVIE.route) { NewMovieScreen(navController = navController) },
                BottomBarDestination(Flows.MOVIE_DETAIL.route, Flows.MOVIE_DETAIL.navArgs) {
                    navController.currentBackStackEntry?.arguments?.getParcelable<Movie>(NavArg.MovieItem.key)?.let {
                        MovieDetailScreen(it)
                    }
                }
            ),
            content = {
                HomeTabScreen(
                    onAddMovieClicked = {
                        if (viewModel.isUserLogged()) navController.navigate(Flows.NEW_MOVIE.route)
                        else viewModel.onNeedLogin()
                    },
                    onMovieClicked = {
                        val json = Uri.encode(GsonUtil.toJson(it))
                        navController.navigate(Flows.MOVIE_DETAIL.route.replace(NavArg.MovieItem.getRouteKey(), json))
                    },
                    onSigninClicked = { navController.navigate(Flows.LOGIN.route) },
                    onRegisterClicked = { navController.navigate(Flows.REGISTER.route) }
                )
            }
        ),
        BottomBarItem(
            title = stringResource(id = R.string.tab_movies_name),
            icon = R.drawable.tkup_movie_bar,
            route = MOVIES_TAB,
            content = {
                if (viewModel.isUserLogged()) MovieTabScreen(
                    navController = navController,
                    onMovieClicked = {
                        val json = Uri.encode(GsonUtil.toJson(it))
                        navController.navigate(Flows.MOVIE_DETAIL.route.replace(NavArg.MovieItem.getRouteKey(), json))
                    })
                else viewModel.onNeedLogin()
            }
        ),
        BottomBarItem(
            title = stringResource(id = R.string.tab_notifications_name),
            icon = R.drawable.tkup_notifications_bar,
            route = NOTIFICATIONS_TAB,
            content = {
                if (viewModel.isUserLogged()) NotificationsTabScreen()
                else viewModel.onNeedLogin()
            }
        ),
        BottomBarItem(
            title = stringResource(id = R.string.tab_profile_name),
            icon = R.drawable.tkup_profile_bar,
            route = PROFILE_TAB,
            destinations = listOf(
                BottomBarDestination(Flows.EDIT_PROFILE.route) { EditProfileScreen(navController = navController) },
                BottomBarDestination(Flows.DELETE_ACCOUNT.route) { DeleteAccountScreen(navController = navController) },
                BottomBarDestination(Flows.CHANGE_PASSWORD.route) { ChangePasswordScreen(navController = navController) }
            ),
            content = {
                if (viewModel.isUserLogged()) ProfileTabScreen(
                    navController = navController,
                    onEditProfileClicked = { navController.navigate(Flows.EDIT_PROFILE.route) },
                    onDeleteClicked = { navController.navigate(Flows.DELETE_ACCOUNT.route) },
                    onChangeClicked = { navController.navigate(Flows.CHANGE_PASSWORD.route) }
                )
                else viewModel.onNeedLogin()
            }
        )
    )

    Scaffold(
        backgroundColor = Background,
        bottomBar = {
            BottomBar(
                navController = navController,
                items = items,
                indicatorConfig = IndicatorConfig()
            )
        },
        content = {
            Box(modifier = Modifier.padding(it)) {
                NavigationGraph(navController = navController, items = items)
            }
            viewModel.onClearNeedLogin()
        }
    )
    // Check for session expired flag
    if (hasSessionExpired) {
        viewModel.logout()
        showSessionExpiredDialog.value = true
        LocalContext.current.getActivity()?.intent?.removeExtra(GracefulCrashHandler.EXTRA_SESSION_EXPIRED)
    }
    ShowSessionExpiredDialog(viewModel, showSessionExpiredDialog)
    // Check session and redirect to login if needed
    if (state.needLogin) {
        LaunchedEffect(key1 = null) {
            navController.navigate(Flows.LOGIN.route)
        }
    }
}

@Composable
fun ShowSessionExpiredDialog(
    viewModel: HomeViewModel,
    showSessionExpiredDialog: MutableState<Boolean>
) {
    if (showSessionExpiredDialog.value) {
        ShowAlertMessage(
            alertMessage = AlertMessage(
                message = stringResource(id = R.string.session_expired)
            ),
            onHideAlertMessage = {
                showSessionExpiredDialog.value = false
                viewModel.onReload()
            },
            backgroundContent = {}
        )
    }
}