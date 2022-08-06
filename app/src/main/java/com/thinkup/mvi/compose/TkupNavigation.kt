package com.thinkup.mvi.compose

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.thinkup.mvi.flows.home.HomeScreen
import com.thinkup.mvi.flows.splash.SplashScreen
import com.thinkup.common.getActivity

@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun Navigation(appState: TkupAppState) {
    NavHost(navController = appState.navController, startDestination = Flows.SPLASH.route) {
        splashNav(appState = appState)
    }
}

@ExperimentalMaterialApi
@ExperimentalFoundationApi
private fun NavGraphBuilder.splashNav(appState: TkupAppState) {
    navigation(
        startDestination = NavCommand.ContentType(Flows.SPLASH).route,
        route = Flows.SPLASH.route,
    ) {
        composable(
            navCommand = NavCommand.ContentType(Flows.SPLASH),
            content = {
                SplashScreen(
                    appState = appState
                )
            }
        )
        composable(
            route = Flows.HOME.route,
            arguments = Flows.HOME.navArgs.map { navArgument(it.key) { type = it.navType } },
            deepLinks = Flows.HOME.deepLinks.map { NavDeepLink(it.uriPattern) },
            content = { HomeScreen() }
        )
    }
}

private fun NavGraphBuilder.composable(
    navCommand: NavCommand,
    content: @Composable (NavBackStackEntry) -> Unit
) {
    composable(
        route = navCommand.route,
        arguments = navCommand.args,
        deepLinks = navCommand.deepLinks
    ) {
        content(it)
    }
}

@Composable
fun handleDeeplLinks(navController: NavController) {
    LocalContext.current.getActivity()?.let { activity ->
        activity.intent?.let { intent ->
            FirebaseDynamicLinks.getInstance()
                .getDynamicLink(intent)
                .addOnSuccessListener(activity) { pendingDynamicLinkData ->
                    // Get deep link from result (may be null if no link is found)
                    var deepLink: Uri? = null
                    if (pendingDynamicLinkData != null) {
                        deepLink = pendingDynamicLinkData.link
                    }
                    deepLink?.let {
                        navController.navigate(deepLink)
                    }
                }
                .addOnFailureListener(activity) { e -> Log.w("TAG", "getDynamicLink:onFailure", e) }
        }
    }
}