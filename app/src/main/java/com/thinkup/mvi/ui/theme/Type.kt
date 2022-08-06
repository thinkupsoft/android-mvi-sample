package com.thinkup.mvi.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.thinkup.mvi.R

val fonts = FontFamily(
    Font(R.font.montserrat_extra_light, weight = FontWeight.ExtraLight),
    Font(R.font.montserrat_light, weight = FontWeight.Light),
    Font(R.font.montserrat_regular),
    Font(R.font.montserrat_medium, weight = FontWeight.Medium),
    Font(R.font.montserrat_thin, weight = FontWeight.Thin),
    Font(R.font.montserrat_semi_bold, weight = FontWeight.SemiBold),
    Font(R.font.montserrat_bold, weight = FontWeight.Bold),
    Font(R.font.montserrat_extra_bold, weight = FontWeight.ExtraBold),
    Font(R.font.montserrat_black, weight = FontWeight.Black)
)

val Typography.darkTitle: TextStyle
    get() = TextStyle(
        color = PrimaryDark,
        fontFamily = fonts,
        fontWeight = FontWeight.SemiBold,
        fontSize = TEXT_BIG,
        lineHeight = TEXT_BIG,
        letterSpacing = TEXT_NO_SPACE
    )

val Typography.detailTitle: TextStyle
    get() = TextStyle(
        color = Color.Black,
        fontFamily = fonts,
        fontWeight = FontWeight.SemiBold,
        fontSize = TEXT_BIG,
        lineHeight = TEXT_BIG,
        letterSpacing = TEXT_NO_SPACE
    )

val Typography.detailText: TextStyle
    get() = TextStyle(
        color = Color.Black,
        fontFamily = fonts,
        fontWeight = FontWeight.Normal,
        fontSize = TEXT_NORMAL,
        lineHeight = TEXT_NORMAL,
        letterSpacing = TEXT_NO_SPACE
    )

val Typography.detailLightText: TextStyle
    get() = TextStyle(
        color = Color.Black,
        fontFamily = fonts,
        fontWeight = FontWeight.Light,
        fontSize = TEXT_LARGE,
        lineHeight = TEXT_LARGE,
        letterSpacing = TEXT_NO_SPACE
    )

val Typography.detailSmallHeader: TextStyle
    get() = TextStyle(
        color = Color.Black,
        fontFamily = fonts,
        fontWeight = FontWeight.Medium,
        fontSize = TEXT_NORMAL,
        lineHeight = TEXT_NORMAL,
        letterSpacing = TEXT_NO_SPACE
    )

val Typography.screenMessage: TextStyle
    get() = TextStyle(
        color = Color.Black,
        fontFamily = fonts,
        fontWeight = FontWeight.Light,
        fontSize = TEXT_X_LARGE,
        lineHeight = TEXT_X_LARGE,
        letterSpacing = TEXT_NO_SPACE
    )

val Typography.inputText: TextStyle
    get() = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.SemiBold,
        fontSize = TEXT_NORMAL,
        lineHeight = TEXT_NORMAL,
        letterSpacing = TEXT_NO_SPACE
    )

val Typography.inputErrorText: TextStyle
    get() = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.SemiBold,
        fontSize = TEXT_VERY_SMALL,
        lineHeight = TEXT_XX_LARGE,
        letterSpacing = TEXT_NO_SPACE,
        color = Color.White
    )

val Typography.inputPlaceholder: TextStyle
    get() = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Medium,
        fontSize = TEXT_NORMAL,
        lineHeight = TEXT_NORMAL,
        letterSpacing = TEXT_NO_SPACE
    )

val Typography.smallLink: TextStyle
    get() = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.SemiBold,
        fontSize = TEXT_SMALL,
        lineHeight = TEXT_NORMAL,
        letterSpacing = TEXT_NO_SPACE,
        textAlign = TextAlign.End
    )

val Typography.smallHeader: TextStyle
    get() = TextStyle(
        color = Color.Black,
        fontFamily = fonts,
        fontWeight = FontWeight.Medium,
        fontSize = TEXT_LARGE,
        lineHeight = TEXT_LARGE,
        letterSpacing = TEXT_NO_SPACE
    )

val Typography.tabSelectedHeader: TextStyle
    get() = TextStyle(
        color = PrimaryDark,
        fontFamily = fonts,
        fontWeight = FontWeight.Medium,
        fontSize = TEXT_LARGE,
        lineHeight = TEXT_LARGE,
        letterSpacing = TEXT_NO_SPACE
    )

val Typography.tabUnselectedHeader: TextStyle
    get() = TextStyle(
        color = PrimaryDark50,
        fontFamily = fonts,
        fontWeight = FontWeight.Normal,
        fontSize = TEXT_LARGE,
        lineHeight = TEXT_LARGE,
        letterSpacing = TEXT_NO_SPACE
    )


val Typography.dialogMessage: TextStyle
    get() = TextStyle(
        color = Color.Black,
        fontFamily = fonts,
        fontWeight = FontWeight.Light,
        fontSize = TEXT_BIG,
        lineHeight = TEXT_VERY_BIG,
        letterSpacing = TEXT_NO_SPACE
    )

val Typography.smallData: TextStyle
    get() = TextStyle(
        color = Color.Black,
        fontFamily = fonts,
        fontWeight = FontWeight.Light,
        fontSize = TEXT_LARGE,
        lineHeight = TEXT_LARGE,
        letterSpacing = TEXT_NO_SPACE
    )

val Typography.linkButton: TextStyle
    get() = TextStyle(
        color = PrimaryRed,
        fontFamily = fonts,
        fontWeight = FontWeight.SemiBold,
        fontSize = TEXT_LARGE,
        lineHeight = TEXT_VERY_BIG,
        letterSpacing = TEXT_NO_SPACE
    )

val Typography.cardTitle: TextStyle
    get() = TextStyle(
        color = Color.Black,
        fontFamily = fonts,
        fontWeight = FontWeight.Light,
        fontSize = TEXT_X_LARGE,
        lineHeight = TEXT_X_LARGE,
        letterSpacing = TEXT_NO_SPACE
    )

val Typography.cardText: TextStyle
    get() = TextStyle(
        color = Color.Black,
        fontFamily = fonts,
        fontWeight = FontWeight.Light,
        fontSize = TEXT_VERY_SMALL,
        lineHeight = TEXT_VERY_SMALL,
        letterSpacing = TEXT_NO_SPACE
    )

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Normal,
        fontSize = TEXT_LARGE,
        lineHeight = TEXT_BIG,
        letterSpacing = TEXT_NO_SPACE
    ),
    headlineLarge = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Light,
        fontSize = TEXT_BIG,
        lineHeight = TEXT_BIG,
        color = PrimaryRed,
        letterSpacing = TEXT_NO_SPACE
    ),
    labelSmall = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Normal,
        fontSize = TEXT_VERY_SMALL,
        lineHeight = TEXT_LARGE,
        color = PrimaryCyan,
        letterSpacing = TEXT_NO_SPACE
    ),
    labelMedium = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Medium,
        fontSize = TEXT_LARGE,
        lineHeight = TEXT_X_LARGE,
        color = Color.White,
        letterSpacing = TEXT_NO_SPACE
    )
)