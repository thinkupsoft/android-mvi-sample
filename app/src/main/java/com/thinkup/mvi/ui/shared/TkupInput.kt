package com.thinkup.mvi.ui.shared

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.thinkup.mvi.R
import com.thinkup.mvi.ui.theme.DIMEN_DOUBLE_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_X_SMALL
import com.thinkup.mvi.ui.theme.Error
import com.thinkup.mvi.ui.theme.PrimaryDark
import com.thinkup.mvi.ui.theme.Typography
import com.thinkup.mvi.ui.theme.inputErrorText
import com.thinkup.mvi.ui.theme.inputPlaceholder
import com.thinkup.mvi.ui.theme.inputText
import com.thinkup.common.empty
import com.thinkup.common.notNegative
import com.thinkup.mvi.ui.theme.DIMEN_XX_NORMAL
import kotlin.math.absoluteValue

data class TkupInputConfig(
    val text: String = String.empty(),
    val placeholder: String = String.empty(),
    val textColor: Color = Color.Black,
    val textStyle: TextStyle = Typography.inputText,
    val placeholderStyle: TextStyle = Typography.inputPlaceholder,
    val placeholderColor: Color = PrimaryDark,
    val cursorColor: Color = PrimaryDark,
    val unfocusedIndicatorColor: Color = PrimaryDark,
    val focusedIndicatorColor: Color = PrimaryDark,
    val backgroundColor: Color = Color.Transparent,
    val keyboardType: KeyboardType = KeyboardType.Text,
    val isPasswordField: Boolean = false,
    val isSingleLine: Boolean = true,
    val maxLength: Int = 255,
    val customVisualTransformation: VisualTransformation? = null,
    val errorMessage: String = String.empty(),
    val imeAction: ImeAction = ImeAction.Next,
    @DrawableRes val trailingIcon: Int? = null,
    val trailingIconColor: Color? = null,
    val onImeActionClicked: () -> Unit = {}
)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TkupInput(
    modifier: Modifier,
    config: TkupInputConfig,
    onChangeValue: (String) -> Unit
) {
    val selection = TextRange((config.text.length).notNegative())
    var text by remember { mutableStateOf(TextFieldValue(config.text, selection)) }
    val passwordVisibility = remember { mutableStateOf(!config.isPasswordField) }
    val isError = config.errorMessage.isNotEmpty()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    if (text.text != config.text) text = TextFieldValue(config.text, selection)

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TextField(
            visualTransformation =
            config.customVisualTransformation ?: if (passwordVisibility.value) VisualTransformation.None
            else PasswordVisualTransformation(),
            modifier = modifier.focusRequester(focusRequester),
            value = text,
            isError = isError,
            singleLine = config.isSingleLine,
            maxLines = if (config.isSingleLine) 1 else 5,
            onValueChange = { newText ->
                if (newText.text.length > config.maxLength) return@TextField
                text = newText
                onChangeValue(newText.text)
            },
            placeholder = {
                Text(
                    text = config.placeholder,
                    style = config.placeholderStyle
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = config.keyboardType,
                imeAction = config.imeAction
            ),
            keyboardActions = KeyboardActions(
                onGo = { keyboardController?.hide(); config.onImeActionClicked() },
                onDone = { keyboardController?.hide(); config.onImeActionClicked() },
                onSearch = { keyboardController?.hide(); config.onImeActionClicked() },
                onSend = { keyboardController?.hide(); config.onImeActionClicked() }
            ),
            colors = TextFieldDefaults.textFieldColors(
                textColor = if (isError) Error else config.textColor,
                placeholderColor = if (isError) Error else config.placeholderColor,
                cursorColor = config.cursorColor,
                unfocusedIndicatorColor = config.unfocusedIndicatorColor,
                focusedIndicatorColor = config.focusedIndicatorColor,
                backgroundColor = config.backgroundColor,
                errorIndicatorColor = Error,
                errorCursorColor = Error
            ),
            textStyle = config.textStyle,
            trailingIcon = {
                if (config.isPasswordField) {
                    IconButton(onClick = {
                        passwordVisibility.value = !passwordVisibility.value
                    }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(
                                id =
                                if (passwordVisibility.value) R.drawable.tkup_pass_off
                                else R.drawable.tkup_pass_on
                            ),
                            contentDescription = "visibility",
                            tint = PrimaryDark
                        )
                    }
                } else if (config.trailingIcon != null) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = config.trailingIcon),
                        contentDescription = "trailingIcon",
                        tint = config.trailingIconColor ?: PrimaryDark
                    )
                }
            }
        )
        if (isError)
            Row(
                modifier = Modifier
                    .height(DIMEN_XX_NORMAL)
                    .padding(start = DIMEN_DOUBLE_NORMAL, end = DIMEN_DOUBLE_NORMAL)
                    .background(Error)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.padding(DIMEN_X_SMALL))
                Text(text = config.errorMessage, style = Typography.inputErrorText)
                Spacer(modifier = Modifier.padding(DIMEN_X_SMALL))
            }
    }
}

class MaskVisualTransformation(private val mask: String) : VisualTransformation {

    private val specialSymbolsIndices = mask.indices.filter { mask[it] != '#' }

    override fun filter(text: AnnotatedString): TransformedText {
        var out = ""
        var maskIndex = 0
        text.forEach { char ->
            while (specialSymbolsIndices.contains(maskIndex)) {
                out += mask[maskIndex]
                maskIndex++
            }
            out += char
            maskIndex++
        }
        return TransformedText(AnnotatedString(out), offsetTranslator())
    }

    private fun offsetTranslator() = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            val offsetValue = offset.absoluteValue
            if (offsetValue == 0) return 0
            var numberOfHashtags = 0
            val masked = mask.takeWhile {
                if (it == '#') numberOfHashtags++
                numberOfHashtags < offsetValue
            }
            return masked.length + 1
        }

        override fun transformedToOriginal(offset: Int): Int {
            return mask.take(offset.absoluteValue).count { it == '#' }
        }
    }
}