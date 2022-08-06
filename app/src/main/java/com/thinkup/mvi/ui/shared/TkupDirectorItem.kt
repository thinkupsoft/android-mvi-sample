package com.thinkup.mvi.ui.shared

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.thinkup.models.app.Actor
import com.thinkup.models.app.Director
import com.thinkup.mvi.R
import com.thinkup.mvi.ui.theme.DIMEN_IMAGE
import com.thinkup.mvi.ui.theme.DIMEN_MIN_IMAGE
import com.thinkup.mvi.ui.theme.DIMEN_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_XX_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_X_SMEDIUM
import com.thinkup.mvi.ui.theme.PrimaryCyan
import com.thinkup.mvi.ui.theme.ThinkUpTheme
import com.thinkup.mvi.ui.theme.Typography
import com.thinkup.mvi.ui.theme.cardTitle

data class TkupDirectorItemConfig(
    val director: Director,
    val horizontalPadding: Dp = DIMEN_XX_NORMAL,
    val verticalPadding: Dp = DIMEN_X_SMEDIUM
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TkupDirectorItem(modifier: Modifier = Modifier, config: TkupDirectorItemConfig) {
    Card(
        modifier = modifier
            .padding(horizontal = config.horizontalPadding, vertical = config.verticalPadding)
            .wrapContentHeight(),
        shape = RoundedCornerShape(DIMEN_XX_NORMAL),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = PrimaryCyan
        ),
        onClick = { }
    ) {

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(config.director.image)
                    .scale(Scale.FIT)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(id = R.drawable.tkup_movie_placeholder),
                contentDescription = "movie image",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = DIMEN_XX_NORMAL, topEnd = DIMEN_XX_NORMAL))
                    .heightIn(min = DIMEN_MIN_IMAGE, max = DIMEN_IMAGE)
                    .fillMaxWidth()
            )

            Text(
                text = config.director.name,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = Typography.cardTitle,
                modifier = Modifier
                    .padding(start = DIMEN_NORMAL, top = DIMEN_X_SMEDIUM, end = DIMEN_NORMAL, bottom = DIMEN_NORMAL)
            )
        }
    }
}

@Preview(backgroundColor = 0xF5F5F5)
@Composable
fun TkupDirectorItemPreview() {
    ThinkUpTheme {
        TkupActorItem(
            config = TkupActorItemConfig(
                actor = Actor(
                    1,
                    "Francis Ford Coppola",
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/f/fb/Francis_Ford_Coppola_-1976_%28cropping%29.jpg/200px-Francis_Ford_Coppola_-1976_%28cropping%29.jpg"
                )
            )
        )
    }
}
