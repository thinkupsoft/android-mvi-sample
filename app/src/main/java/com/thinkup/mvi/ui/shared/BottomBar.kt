package com.thinkup.mvi.ui.shared

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.thinkup.mvi.compose.DeepLink
import com.thinkup.mvi.compose.NavArg
import com.thinkup.mvi.ui.theme.DIMEN_NO_SPACE
import com.thinkup.mvi.ui.theme.DIMEN_SMEDIUM
import com.thinkup.mvi.ui.theme.DIMEN_X_SMEDIUM
import com.thinkup.mvi.ui.theme.DIMEN_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_XXX_SMALL
import com.thinkup.mvi.ui.theme.DIMEN_X_SMALL
import com.thinkup.mvi.ui.theme.DIMEN_XXX_BIG
import com.thinkup.mvi.ui.theme.PrimaryCyan
import com.thinkup.mvi.ui.theme.PrimaryDark
import com.thinkup.mvi.ui.theme.Typography

data class BottomBarItem(
    val title: String,
    val icon: Int,
    val route: String,
    val navArgs: List<NavArg> = listOf(),
    val destinations: List<BottomBarDestination> = listOf(),
    val deepLink: List<DeepLink> = listOf(),
    val content: @Composable () -> Unit,
)

data class BottomBarDestination(
    val route: String,
    val navArgs: List<NavArg> = listOf(),
    val deepLink: List<DeepLink> = listOf(),
    val content: @Composable () -> Unit
)

data class IndicatorConfig(
    val show: Boolean = true,
    val color: Color = PrimaryCyan,
    val animDuration: Int = 350,
    val animDelay: Int = 50,
    val paddingTop: Dp = DIMEN_X_SMALL,
    val roundedDp: Dp = DIMEN_SMEDIUM,
    val width: Dp = DIMEN_NORMAL,
    val height: Dp = DIMEN_XXX_SMALL
)

@Composable
fun BottomBar(
    navController: NavController,
    items: List<BottomBarItem>,
    indicatorConfig: IndicatorConfig
) {
    // last index selected state
    var selectedIndex by remember { mutableStateOf(0) }
    val currentScreen by navController.currentScreen(items)
    if (currentScreen) {
        BottomNavigation(
            backgroundColor = PrimaryDark,
            contentColor = Color.White,
            elevation = DIMEN_NO_SPACE,
            modifier = Modifier.height(DIMEN_XXX_BIG)
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            items.forEachIndexed { index, item ->
                val isSelected = currentRoute == item.route
                selectedIndex = if (isSelected) index else selectedIndex
                val color = if (isSelected) PrimaryCyan else Color.White
                BottomNavigationItem(
                    modifier = Modifier.padding(bottom = DIMEN_X_SMEDIUM),
                    icon = {
                        Icon(
                            painterResource(id = item.icon),
                            contentDescription = item.title,
                            modifier = Modifier
                                .padding(bottom = DIMEN_SMEDIUM)
                        )
                    },
                    label = {
                        Text(
                            text = item.title,
                            style = Typography.labelSmall,
                            color = color,
                        )
                    },
                    selectedContentColor = PrimaryCyan,
                    unselectedContentColor = Color.White,
                    alwaysShowLabel = true,
                    selected = isSelected,
                    onClick = {
                        navController.navigate(item.route) {
                            navController.graph.startDestinationRoute?.let { screen_route ->
                                popUpTo(screen_route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
        BottomBarIndicator(modifier = Modifier, config = indicatorConfig, items.size, selectedIndex)
    } else Unit
}

@Composable
fun NavigationGraph(navController: NavHostController, items: List<BottomBarItem>) {
    val firstItem = items.first()
    NavHost(navController, startDestination = firstItem.route) {
        items.forEach { item ->
            composable(
                route = item.route,
                arguments = item.navArgs.map { navArgument(it.key) { type = it.navType } },
                deepLinks = item.deepLink.map { NavDeepLink(it.uriPattern) }
            ) {
                item.content()
            }
            item.destinations.forEach { destination ->
                composable(
                    route = destination.route,
                    arguments = destination.navArgs.map { navArgument(it.key) { type = it.navType } },
                    deepLinks = destination.deepLink.map { NavDeepLink(it.uriPattern) }
                ) {
                    destination.content()
                }
            }
        }
    }
}

@Composable
private fun NavController.currentScreen(items: List<BottomBarItem>): State<Boolean> {
    val currentScreen = remember { mutableStateOf(true) }

    DisposableEffect(key1 = this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            when {
                destination.hierarchy.any { items.map { it.route }.contains(it.route) } -> {
                    currentScreen.value = true
                }
                else -> currentScreen.value = false
            }
        }
        addOnDestinationChangedListener(listener)
        this.onDispose { }
    }
    return currentScreen
}