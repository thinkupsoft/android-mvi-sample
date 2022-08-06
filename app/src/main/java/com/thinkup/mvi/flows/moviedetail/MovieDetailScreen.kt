package com.thinkup.mvi.flows.moviedetail

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.thinkup.mvi.R
import com.thinkup.mvi.ui.shared.TkupCarrousel
import com.thinkup.mvi.ui.shared.TkupCarrouselConfig
import com.thinkup.mvi.ui.theme.DIMEN_NO_SPACE
import com.thinkup.mvi.ui.theme.DIMEN_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_HEADER_IMAGE
import com.thinkup.mvi.ui.theme.DIMEN_XX_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_DOUBLE_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_X_SMALL
import com.thinkup.mvi.ui.theme.Gray1
import com.thinkup.mvi.ui.theme.PrimaryCyan18
import com.thinkup.mvi.ui.theme.Typography
import com.thinkup.mvi.ui.theme.detailLightText
import com.thinkup.mvi.ui.theme.detailSmallHeader
import com.thinkup.mvi.ui.theme.detailText
import com.thinkup.mvi.ui.theme.detailTitle
import com.thinkup.models.app.Movie

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MovieDetailScreen(movie: Movie) {
    WindowCompat.setDecorFitsSystemWindows((LocalContext.current as ComponentActivity).window, true)
    Column(Modifier.fillMaxSize()) {

        Box(Modifier.wrapContentHeight()) {
            val pagerState = rememberPagerState()

            TkupCarrousel(
                config = TkupCarrouselConfig(
                    state = pagerState,
                    horizontalPadding = DIMEN_NO_SPACE,
                    itemSpacing = DIMEN_NO_SPACE,
                    content = movie.images.map {
                        {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(it.imageUrl)
                                    .scale(Scale.FIT)
                                    .crossfade(true)
                                    .build(),
                                placeholder = painterResource(id = R.drawable.tkup_movie_placeholder),
                                contentDescription = "movie image",
                                contentScale = ContentScale.FillHeight,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(PrimaryCyan18)
                                    .height(DIMEN_HEADER_IMAGE)
                            )
                        }
                    }
                )
            )

            HorizontalPagerIndicator(
                activeColor = Color.White,
                inactiveColor = Gray1,
                pagerState = pagerState,
                indicatorHeight = DIMEN_X_SMALL,
                indicatorWidth = DIMEN_X_SMALL,
                spacing = DIMEN_X_SMALL,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(DIMEN_NORMAL)
            )
        }

        Text(
            text = movie.title,
            modifier = Modifier
                .padding(horizontal = DIMEN_XX_NORMAL)
                .padding(top = DIMEN_DOUBLE_NORMAL),
            style = Typography.detailTitle
        )
        Text(
            text = movie.subtitle.orEmpty(),
            modifier = Modifier
                .padding(horizontal = DIMEN_XX_NORMAL)
                .padding(top = DIMEN_X_SMALL),
            style = Typography.detailText
        )
        Text(
            text = movie.actors,
            modifier = Modifier
                .padding(horizontal = DIMEN_XX_NORMAL)
                .padding(top = DIMEN_NORMAL),
            style = Typography.bodyLarge,
            color = Color.Black
        )
        Divider(
            Modifier
                .padding(horizontal = DIMEN_XX_NORMAL)
                .padding(top = DIMEN_NORMAL)
        )
        Text(
            text = stringResource(id = R.string.new_movie_director),
            modifier = Modifier
                .padding(horizontal = DIMEN_XX_NORMAL)
                .padding(top = DIMEN_DOUBLE_NORMAL),
            style = Typography.detailSmallHeader
        )
        Text(
            text = movie.director.orEmpty(),
            modifier = Modifier.padding(horizontal = DIMEN_XX_NORMAL),
            style = Typography.detailLightText
        )
        Text(
            text = stringResource(id = R.string.new_movie_description),
            modifier = Modifier
                .padding(horizontal = DIMEN_XX_NORMAL)
                .padding(top = DIMEN_XX_NORMAL),
            style = Typography.detailSmallHeader
        )
        Text(
            text = movie.description.orEmpty(),
            modifier = Modifier.padding(horizontal = DIMEN_XX_NORMAL),
            style = Typography.detailLightText
        )

    }
}