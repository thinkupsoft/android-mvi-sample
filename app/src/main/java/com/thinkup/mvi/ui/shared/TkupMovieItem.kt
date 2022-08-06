package com.thinkup.mvi.ui.shared

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.thinkup.mvi.R
import com.thinkup.mvi.ui.theme.DIMEN_IMAGE
import com.thinkup.mvi.ui.theme.DIMEN_X_SMEDIUM
import com.thinkup.mvi.ui.theme.DIMEN_BIG_IMAGE
import com.thinkup.mvi.ui.theme.DIMEN_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_XX_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_X_SMALL
import com.thinkup.mvi.ui.theme.PrimaryCyan
import com.thinkup.mvi.ui.theme.ThinkUpTheme
import com.thinkup.mvi.ui.theme.Typography
import com.thinkup.mvi.ui.theme.cardText
import com.thinkup.mvi.ui.theme.cardTitle
import com.thinkup.models.app.Movie
import com.thinkup.models.app.MovieImage

data class TkupMovieItemConfig(
    val movie: Movie,
    val horizontalPadding: Dp = DIMEN_XX_NORMAL,
    val verticalPadding: Dp = DIMEN_X_SMEDIUM,
    val onClick: (Movie) -> Unit = {}
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TkupMovieItem(modifier: Modifier = Modifier, config: TkupMovieItemConfig) {
    val resourcesHelper = rememberResourceHelper()
    Card(
        modifier = modifier
            .padding(horizontal = config.horizontalPadding, vertical = config.verticalPadding)
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        shape = RoundedCornerShape(DIMEN_XX_NORMAL),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = PrimaryCyan
        ),
        onClick = { config.onClick.invoke(config.movie) }
    ) {
        Row {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(config.movie.images[0].imageUrl)
                    .scale(Scale.FIT)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(id = R.drawable.tkup_movie_placeholder),
                contentDescription = "movie image",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = DIMEN_XX_NORMAL, bottomStart = DIMEN_XX_NORMAL))
                    .heightIn(min = DIMEN_IMAGE, max = DIMEN_BIG_IMAGE)
                    .width(DIMEN_IMAGE)
                    .fillMaxHeight()
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = DIMEN_NORMAL, top = DIMEN_X_SMEDIUM)
            ) {
                Text(
                    text = config.movie.title,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = Typography.cardTitle,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Text(
                    text = config.movie.actors,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = Typography.cardText,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = DIMEN_X_SMALL)
                )
                val publication = config.movie.publicationDate?.let {
                    resourcesHelper.formatServerDate(it)
                } ?: run { stringResource(id = R.string.no_data) }
                Text(
                    text = "${stringResource(id = R.string.my_movies_published_on)} $publication",
                    style = Typography.cardText,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = DIMEN_X_SMALL, bottom = DIMEN_X_SMALL)
                )
            }
        }
    }
}

@Preview(backgroundColor = 0xF5F5F5)
@Composable
fun TkupMovieItemPreview() {
    ThinkUpTheme {
        TkupMovieItem(
            config = TkupMovieItemConfig(
                movie = Movie(
                    2,
                    "The Godfather",
                    "",
                    "Marlon Brando, Al Pacino, James Caan",
                    "Francis Ford Coppola",
                    "1972-01-01",
                    "The aging patriarch of an organized crime dynasty in postwar New York City transfers control of his clandestine empire to his reluctant youngest son.",
                    true,
                    "2022-06-09T20:24:38.000Z",
                    listOf(
                        MovieImage(
                            "https://m.media-amazon.com/images/M/MV5BM2MyNjYxNmUtYTAwNi00MTYxLWJmNWYtYzZlODY3ZTk3OTFlXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_SX300.jpg"
                        )
                    ),
                    listOf()
                )
            )
        )
    }
}
