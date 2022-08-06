package com.thinkup.mvi.exception

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.thinkup.mvi.BuildConfig
import com.thinkup.mvi.MainActivity
import com.thinkup.mvi.R
import com.thinkup.mvi.ui.shared.TkupButton
import com.thinkup.mvi.ui.shared.TkupButtonConfig
import com.thinkup.mvi.ui.shared.TkupPaddings
import com.thinkup.mvi.ui.theme.ThinkUpTheme
import com.thinkup.mvi.ui.theme.DIMEN_VERY_BIG_IMAGE
import com.thinkup.mvi.ui.theme.DIMEN_XX_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_DOUBLE_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_TRIPLE_NORMAL
import com.thinkup.mvi.ui.theme.Gray2
import com.thinkup.mvi.ui.theme.PrimaryCyan
import com.thinkup.mvi.ui.theme.Typography

class CrashActivity : ComponentActivity() {

    companion object {
        private const val DATA = "tkup_data_error"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ThinkUpTheme {
                Surface(color = PrimaryCyan) {
                    CrashScreen()
                }
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun CrashScreen() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(PrimaryCyan)
        ) {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .padding(start = DIMEN_XX_NORMAL, end = DIMEN_XX_NORMAL, top = DIMEN_DOUBLE_NORMAL)
                    .weight(1f)
            ) {

                item {
                    Image(
                        bitmap = ImageBitmap.imageResource(id = R.drawable.tkup_logo),
                        contentDescription = "logo",
                        modifier = Modifier.size(DIMEN_VERY_BIG_IMAGE)
                    )

                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(DIMEN_DOUBLE_NORMAL)
                    )
                    Text(text = stringResource(id = R.string.app_error), style = Typography.labelMedium)
                    if (BuildConfig.DEBUG) {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .size(DIMEN_XX_NORMAL)
                        )
                        val error = intent.getSerializableExtra(GracefulCrashHandler.EXTRA_ERROR) as Exception?
                        Text(
                            text = error?.stackTraceToString().orEmpty(),
                            style = Typography.labelMedium,
                            modifier = Modifier.combinedClickable(
                                onClick = {},
                                onLongClick = { setClipboard(error?.stackTraceToString().orEmpty()) }
                            )
                        )
                    }
                }
            }
            TkupButton(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(start = DIMEN_XX_NORMAL, end = DIMEN_XX_NORMAL, bottom = DIMEN_XX_NORMAL, top = DIMEN_XX_NORMAL),
                config = TkupButtonConfig(
                    text = stringResource(id = R.string.retry),
                    textColor = PrimaryCyan,
                    background = Gray2,
                    paddings = TkupPaddings(bottom = DIMEN_XX_NORMAL, start = DIMEN_TRIPLE_NORMAL, end = DIMEN_TRIPLE_NORMAL)
                )
            ) { onClick() }
        }
    }

    private fun setClipboard(error: String): Boolean {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val data = ClipData.newPlainText(DATA, error)
        clipboard.setPrimaryClip(data);
        Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
        return true
    }

    private fun onClick() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun CrashScreenPreview() {
        ThinkUpTheme {
            CrashScreen()
        }
    }
}