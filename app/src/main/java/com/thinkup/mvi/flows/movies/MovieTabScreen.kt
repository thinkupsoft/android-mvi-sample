package com.thinkup.mvi.flows.movies

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.thinkup.models.app.Movie
import com.thinkup.mvi.R
import com.thinkup.mvi.ui.shared.TkupTabItem
import com.thinkup.mvi.ui.shared.TkupTabLayout
import com.thinkup.mvi.ui.shared.TkupTabLayoutConfig
import com.thinkup.mvi.ui.theme.DIMEN_NORMAL
import com.thinkup.mvi.ui.theme.ThinkUpTheme

@Composable
fun MovieTabScreen(
    navController: NavController,
    onMovieClicked: ((Movie) -> Unit)? = null
) {
    TkupTabLayout(
        modifier = Modifier,
        config = TkupTabLayoutConfig(
            indicatorPadding = DIMEN_NORMAL,
            items = listOf(
                TkupTabItem(
                    title = stringResource(id = R.string.my_movies_tab_one),
                    selected = true,
                    content = { MyMoviesTab(navController = navController, onMovieClicked = onMovieClicked) }
                ),
                TkupTabItem(
                    title = stringResource(id = R.string.my_movies_tab_two),
                    selected = false,
                    content = { ActorsTab() }
                ),
                TkupTabItem(
                    title = stringResource(id = R.string.my_movies_tab_three),
                    selected = false,
                    content = { DirectorsTab() }
                )
            )
        )
    )
}

@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xFFFFFF)
@Composable
fun MovieTabScreenPreview() {
    ThinkUpTheme {
        MovieTabScreen(rememberNavController())
    }
}