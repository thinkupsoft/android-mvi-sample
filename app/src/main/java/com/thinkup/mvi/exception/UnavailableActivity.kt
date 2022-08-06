package com.thinkup.mvi.exception

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.thinkup.mvi.MainActivity
import com.thinkup.mvi.R
import com.thinkup.mvi.ui.shared.TkupButton
import com.thinkup.mvi.ui.shared.TkupButtonConfig
import com.thinkup.mvi.ui.theme.Background
import com.thinkup.mvi.ui.theme.DIMEN_DOUBLE_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_TRIPLE_NORMAL
import com.thinkup.mvi.ui.theme.PrimaryDark
import com.thinkup.mvi.ui.theme.Typography
import com.thinkup.mvi.ui.theme.screenMessage

class UnavailableActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UnavailableServiceScreen()
        }
    }

    private fun onClick() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    @Preview
    @Composable
    fun UnavailableServiceScreen() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(DIMEN_TRIPLE_NORMAL)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.tkup_service_unavailable),
                    contentDescription = "logo"
                )
                Divider(modifier = Modifier.padding(DIMEN_DOUBLE_NORMAL))
                Text(
                    text = stringResource(id = R.string.app_503_error_title),
                    style = Typography.titleMedium,
                    textAlign = TextAlign.Center
                )
                Divider(modifier = Modifier.padding(DIMEN_DOUBLE_NORMAL))
                Text(
                    text = stringResource(id = R.string.app_503_error_message),
                    style = Typography.screenMessage,
                    textAlign = TextAlign.Center
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                contentAlignment = Alignment.Center
            ) {
                TkupButton(
                    modifier = Modifier,
                    config = TkupButtonConfig(
                        text = stringResource(id = R.string.retry),
                        background = PrimaryDark,
                        textColor = Color.White
                    )
                ) { onClick() }
            }
        }
    }
}