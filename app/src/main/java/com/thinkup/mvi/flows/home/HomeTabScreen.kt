package com.thinkup.mvi.flows.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.thinkup.mvi.R
import com.thinkup.mvi.ui.shared.TkupMovieItem
import com.thinkup.mvi.ui.shared.TkupMovieItemConfig
import com.thinkup.mvi.ui.shared.TkupButton
import com.thinkup.mvi.ui.shared.TkupButtonConfig
import com.thinkup.mvi.ui.shared.TkupCarrousel
import com.thinkup.mvi.ui.shared.TkupCarrouselConfig
import com.thinkup.mvi.ui.shared.TkupEmptyView
import com.thinkup.mvi.ui.shared.TkupEmptyViewConfig
import com.thinkup.mvi.ui.shared.TkupNavigationTitle
import com.thinkup.mvi.ui.shared.TkupPaddings
import com.thinkup.mvi.ui.shared.TkupShimmer
import com.thinkup.mvi.ui.shared.TkupShimmerConfig
import com.thinkup.mvi.ui.theme.ThinkUpTheme
import com.thinkup.mvi.ui.theme.DIMEN_NO_SPACE
import com.thinkup.mvi.ui.theme.DIMEN_IMAGE
import com.thinkup.mvi.ui.theme.DIMEN_SMEDIUM
import com.thinkup.mvi.ui.theme.DIMEN_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_MIN_SPACE
import com.thinkup.mvi.ui.theme.DIMEN_XX_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_X_MEDIUM
import com.thinkup.mvi.ui.theme.DIMEN_TRIPLE_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_BIG
import com.thinkup.mvi.ui.theme.DIMEN_XX_BIG
import com.thinkup.mvi.ui.theme.Gray1
import com.thinkup.mvi.ui.theme.Gray5
import com.thinkup.mvi.ui.theme.PrimaryDark
import com.thinkup.mvi.ui.theme.PrimaryRed
import com.thinkup.mvi.ui.theme.TEXT_LARGE
import com.thinkup.common.whitespace
import com.thinkup.models.app.Movie

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeTabScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onAddMovieClicked: (() -> Unit)? = null,
    onSigninClicked: (() -> Unit)? = null,
    onRegisterClicked: (() -> Unit)? = null,
    onMovieClicked: ((Movie) -> Unit)? = null
) {
    val state by viewModel.state.collectAsState()
    val openLoginDialog = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
    ) {
        LazyColumn(
            Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .weight(1f)
        ) {
            item {
                TkupNavigationTitle(
                    title = stringResource(id = R.string.home_title_featured),
                    modifier = Modifier.padding(bottom = DIMEN_XX_NORMAL),
                    showBAckArrow = false
                )
                TkupCarrousel(
                    config = TkupCarrouselConfig(
                        state = rememberPagerState(),
                        horizontalPadding = DIMEN_XX_BIG,
                        stopPercentage = 1.0f,
                        content =
                        if (state.isLoadingFM) GetShimmerItems()
                        else GetFeaturedMovies(state.featuredMovies, onMovieClicked)
                    )
                )
            }
            if (viewModel.isUserLogged()) {
                viewModel.loadMyMovies()
                item {
                    TkupNavigationTitle(
                        title = stringResource(id = R.string.home_title_my_movies),
                        modifier = Modifier.padding(bottom = DIMEN_XX_NORMAL),
                        showBAckArrow = false
                    )
                    TkupCarrousel(
                        config = TkupCarrouselConfig(
                            state = rememberPagerState(),
                            horizontalPadding = DIMEN_XX_BIG,
                            stopPercentage = 1.0f,
                            content =
                            if (state.isLoadingMM) GetShimmerItems()
                            else GetMyMovies(state.myMovies, onMovieClicked, onAddMovieClicked)
                        )
                    )
                }
            }
        }
        if (!viewModel.isUserLogged()) {
            // Separator
            Divider(
                color = Gray5,
                thickness = DIMEN_MIN_SPACE,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = DIMEN_X_MEDIUM)
            )

            // Sign-in button
            TkupButton(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = DIMEN_NORMAL),
                config = TkupButtonConfig(
                    text = stringResource(id = R.string.sign_in_uppercase),
                    paddings = TkupPaddings(start = DIMEN_BIG, end = DIMEN_BIG)
                ),
                onClick = { openLoginDialog.value = true }
            )

            // Register link
            val annotatedString = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = PrimaryDark,
                        fontWeight = FontWeight.Normal,
                        fontSize = TEXT_LARGE
                    )
                ) { append(stringResource(id = R.string.home_register_link1)) }
                withStyle(
                    style = SpanStyle(
                        color = PrimaryRed,
                        fontWeight = FontWeight.Medium,
                        fontSize = TEXT_LARGE
                    )
                ) { append("${String.whitespace()} ${stringResource(id = R.string.home_register_link2)}") }
            }
            ClickableText(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = DIMEN_NORMAL, bottom = DIMEN_NORMAL),
                text = annotatedString
            ) {
                onRegisterClicked?.invoke()
            }
        }
    }
    if (openLoginDialog.value) onSigninClicked?.invoke()
}

@Composable
fun GetFeaturedMovies(
    featuredMovies: List<Movie>,
    onMovieClicked: ((Movie) -> Unit)?
): List<@Composable () -> Unit> {
    return if (featuredMovies.isEmpty()) listOf {
        TkupEmptyView(
            modifier = Modifier.fillMaxWidth(),
            config = TkupEmptyViewConfig(
                message = stringResource(id = R.string.home_empty_featured),
                paddings = TkupPaddings(top = DIMEN_NO_SPACE, bottom = DIMEN_NO_SPACE)
            )
        )
    }
    else featuredMovies.map {
        {
            TkupMovieItem(
                modifier = Modifier.fillMaxWidth(),
                config = TkupMovieItemConfig(
                    movie = it,
                    horizontalPadding = DIMEN_NO_SPACE,
                    verticalPadding = DIMEN_NO_SPACE,
                ) { onMovieClicked?.invoke(it) }
            )
        }
    }
}

@Composable
fun GetMyMovies(
    myMovies: List<Movie>,
    onMovieClicked: ((Movie) -> Unit)?,
    onAddMovieClicked: (() -> Unit)?
): List<@Composable () -> Unit> {
    return if (myMovies.isEmpty()) listOf {
        TkupEmptyView(
            modifier = Modifier.fillMaxWidth(),
            config = TkupEmptyViewConfig(
                message = stringResource(id = R.string.my_movies_empty_message),
                paddings = TkupPaddings(top = DIMEN_NO_SPACE, bottom = DIMEN_NO_SPACE),
                buttonConfig = TkupButtonConfig(
                    background = Gray1,
                    text = stringResource(id = R.string.my_movies_new_movie),
                    icon = R.drawable.tkup_new_image_medium,
                    paddings = TkupPaddings(DIMEN_SMEDIUM, DIMEN_SMEDIUM, DIMEN_TRIPLE_NORMAL, DIMEN_XX_NORMAL)
                )
            ) { onAddMovieClicked?.invoke() }
        )
    }
    else myMovies.map {
        {
            TkupMovieItem(
                modifier = Modifier.fillMaxWidth(),
                config = TkupMovieItemConfig(
                    movie = it,
                    horizontalPadding = DIMEN_NO_SPACE,
                    verticalPadding = DIMEN_NO_SPACE,
                ) { onMovieClicked?.invoke(it) }
            )
        }
    }
}

@Composable
fun GetShimmerItems(): List<@Composable () -> Unit> {
    return listOf(1, 2, 3, 4, 5).map {
        {
            TkupShimmer(config = TkupShimmerConfig { brush ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(brush, RoundedCornerShape(DIMEN_XX_NORMAL))
                        .heightIn(min = DIMEN_IMAGE)
                )
            })
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xFFFFFF)
@Composable
fun HomeTabScreenPreview() {
    ThinkUpTheme {
        HomeTabScreen()
    }
}