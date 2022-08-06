package com.thinkup.mvi.flows.movies

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.thinkup.mvi.R
import com.thinkup.mvi.compose.Flows
import com.thinkup.mvi.ui.SCREEN_RESULT
import com.thinkup.mvi.ui.shared.TkupMovieItem
import com.thinkup.mvi.ui.shared.TkupMovieItemConfig
import com.thinkup.mvi.ui.shared.TkupButton
import com.thinkup.mvi.ui.shared.TkupButtonConfig
import com.thinkup.mvi.ui.shared.TkupEmptyView
import com.thinkup.mvi.ui.shared.TkupEmptyViewConfig
import com.thinkup.mvi.ui.shared.TkupPaddings
import com.thinkup.mvi.ui.shared.FooterErrorState
import com.thinkup.mvi.ui.shared.FooterLoadingState
import com.thinkup.mvi.ui.shared.NoInternetState
import com.thinkup.mvi.ui.shared.TkupPagedList
import com.thinkup.mvi.ui.shared.TkupPagingItem
import com.thinkup.mvi.ui.theme.DIMEN_SMEDIUM
import com.thinkup.mvi.ui.theme.DIMEN_X_SMEDIUM
import com.thinkup.mvi.ui.theme.DIMEN_XX_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_TRIPLE_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_BUTTON
import com.thinkup.mvi.ui.theme.Gray1
import com.thinkup.models.app.Movie

@Composable
fun MyMoviesTab(
    viewModel: MoviesViewModel = hiltViewModel(),
    navController: NavController,
    onMovieClicked: ((Movie) -> Unit)? = null
) {
    val state by viewModel.state.collectAsState()

    val screenResult = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<Boolean>(SCREEN_RESULT)
    screenResult?.value?.let {
        if (it) {
            navController.currentBackStackEntry?.savedStateHandle?.set(SCREEN_RESULT, false)
            viewModel.onReload()
        }
    }

    if (state.noInternetClicked >= 0)
        TkupPagedList(
            modifier = Modifier
                .fillMaxSize(),
            viewModel = viewModel.pagingViewModel,
            itemType = MovieItemTkup(),
            emptyState = { MovieEmptyState { navController.navigate(Flows.NEW_MOVIE.route) } },
            noInternetState = { NoInternetState { viewModel.onReload() } },
            footerLoadingState = { FooterLoadingState() },
            footerErrorState = { FooterErrorState() },
            onClick = { data, _ ->
                when (data) {
                    is Movie -> onMovieClicked?.invoke(data)
                    MoviesTkupPagingViewModel.Header -> navController.navigate(Flows.NEW_MOVIE.route)
                }
            }
        )
}

@Composable
fun MovieEmptyState(onClick: () -> Unit = {}) {
    TkupEmptyView(
        config = TkupEmptyViewConfig(
            message = stringResource(id = R.string.my_movies_empty_message),
            icon = R.drawable.tkup_empty_movies,
            buttonConfig = TkupButtonConfig(
                background = Gray1,
                text = stringResource(id = R.string.my_movies_new_movie),
                icon = R.drawable.tkup_new_image_medium,
                paddings = TkupPaddings(DIMEN_SMEDIUM, DIMEN_SMEDIUM, DIMEN_TRIPLE_NORMAL, DIMEN_XX_NORMAL)
            ),
            onButtonClick = onClick
        )
    )
}

class MovieItemTkup : TkupPagingItem {

    @Composable
    override fun GetItem(data: Any, index: Int, onClick: ((Any, Int) -> Unit)?) {
        when (data) {
            is Movie -> TkupMovieItem(config = TkupMovieItemConfig(data) { onClick?.invoke(data, index) })
            MoviesTkupPagingViewModel.Header -> TkupButton(
                modifier = Modifier,
                config = TkupButtonConfig(
                    background = Gray1,
                    text = stringResource(id = R.string.put_movie_uppercase),
                    icon = R.drawable.tkup_new_image_medium,
                    paddings = TkupPaddings(DIMEN_BUTTON, DIMEN_BUTTON, DIMEN_XX_NORMAL, DIMEN_X_SMEDIUM)
                )
            ) { onClick?.invoke(data, index) }
        }
    }
}