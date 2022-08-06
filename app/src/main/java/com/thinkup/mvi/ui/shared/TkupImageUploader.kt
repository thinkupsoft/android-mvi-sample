package com.thinkup.mvi.ui.shared

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Scale
import com.thinkup.mvi.R
import com.thinkup.mvi.ui.theme.ThinkUpTheme
import com.thinkup.mvi.ui.theme.DIMEN_NO_SPACE
import com.thinkup.mvi.ui.theme.DIMEN_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_X_BIG
import com.thinkup.mvi.ui.theme.DIMEN_XX_SMALL
import com.thinkup.mvi.ui.theme.DIMEN_MIN_IMAGE
import com.thinkup.mvi.ui.theme.PrimaryCyan
import com.thinkup.mvi.ui.theme.PrimaryCyan18

data class TkupImageUploaderConfig(
    val bitmap: Bitmap? = null,
    val uri: Uri? = null,
    val onClick: () -> Unit = {}
)

@Composable
fun TkupImageUploader(config: TkupImageUploaderConfig) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(DIMEN_NORMAL))
            .size(DIMEN_MIN_IMAGE)
            .background(Color.White)
            .clickable { config.onClick() }
    ) {
        val stroke = Stroke(
            width = DIMEN_XX_SMALL.value,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(DIMEN_NORMAL.value, DIMEN_NORMAL.value), DIMEN_NO_SPACE.value)
        )
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .background(PrimaryCyan18)
        ) {
            drawRoundRect(color = PrimaryCyan, style = stroke, cornerRadius = CornerRadius(DIMEN_X_BIG.value))
        }
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.tkup_new_image),
            contentDescription = "new image",
            modifier = Modifier.fillMaxSize()
        )
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(config.bitmap ?: config.uri)
                .decoderFactory(SvgDecoder.Factory())
                .scale(Scale.FIT)
                .crossfade(true)
                .build(),
            contentDescription = "movie image",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(DIMEN_NORMAL))
                .size(DIMEN_MIN_IMAGE)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TkupImageUploaderPreview() {
    ThinkUpTheme(darkTheme = false) {
        TkupImageUploader(
            TkupImageUploaderConfig()
        )
    }
}