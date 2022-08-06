package com.thinkup.common.ui

import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.TextView

object Spannable {
    fun setText(
        view: TextView, start: String, end: String,
        colorStart: Int, colorEnd: Int,
        fontStart: Int, fontEnd: Int
    ) {
        val spannable = SpannableString("$start$end")

        spannable.setSpan(ForegroundColorSpan(getColorStart(end, colorStart, colorEnd)), 0, start.length, 0)
        spannable.setSpan(CustomTypefaceSpan(view.context, fontStart), 0, start.length, 0)

        spannable.setSpan(ForegroundColorSpan(colorEnd), start.length, start.length + end.length, 0)
        spannable.setSpan(CustomTypefaceSpan(view.context, fontEnd), start.length, start.length + end.length, 0)

        view.setText(spannable, TextView.BufferType.SPANNABLE)
    }

    fun setText(
        view: TextView, texts: Array<String>,
        colorStart: Int, colorEnd: Int,
        fontStart: Int, fontEnd: Int
    ) {
        val spannable = SpannableString(texts.joinToString(separator = "") { t -> t })

        var start = true
        var offset = 0
        for (text in texts) {
            if (start) {
                spannable.setSpan(ForegroundColorSpan(colorStart), offset, offset + text.length, 0)
                spannable.setSpan(CustomTypefaceSpan(view.context, fontStart), offset, offset + text.length, 0)
            } else {
                spannable.setSpan(ForegroundColorSpan(colorEnd), offset, offset + text.length, 0)
                spannable.setSpan(CustomTypefaceSpan(view.context, fontEnd), offset, offset + text.length, 0)
            }
            start = !start
            offset += text.length
        }

        view.setText(spannable, TextView.BufferType.SPANNABLE)
    }

    fun setText(
        view: TextView, start: String, center: String, end: String,
        colorStart: Int, colorCenter: Int, colorEnd: Int,
        fontStart: Int, fontCenter: Int , fontEnd: Int
    ) {
        val spannable = SpannableString("$start$center$end")

        spannable.setSpan(ForegroundColorSpan(getColorStart(end, colorStart, colorCenter)), 0, start.length, 0)
        spannable.setSpan(CustomTypefaceSpan(view.context, fontStart), 0, start.length, 0)

        spannable.setSpan(ForegroundColorSpan(getColorStart(end, colorCenter, colorEnd)), start.length, start.length + center.length, 0)
        spannable.setSpan(CustomTypefaceSpan(view.context, fontCenter), start.length, start.length + center.length, 0)

        spannable.setSpan(ForegroundColorSpan(colorEnd), start.length + center.length, start.length + center.length + end.length, 0)
        spannable.setSpan(CustomTypefaceSpan(view.context, fontEnd), start.length + center.length, start.length + center.length + end.length, 0)

        view.setText(spannable, TextView.BufferType.SPANNABLE)
    }

    private fun getColorStart(end: String, colorStart: Int, colorEnd: Int): Int {
        return if (end.isEmpty()) colorEnd else colorStart
    }

    fun setBold(view: TextView, start: String, end: String) {
        val spannable = SpannableString("$start$end")
        spannable.setSpan(
            android.text.style.StyleSpan(Typeface.BOLD),
            start.length,
            start.length + end.length,
            android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        view.setText(spannable, TextView.BufferType.SPANNABLE)
    }

    fun setBold(view: TextView, text: String, start: Int, end: Int) {
        val spannable = SpannableString(text)
        spannable.setSpan(
            android.text.style.StyleSpan(Typeface.BOLD),
            start,
            end,
            android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        view.setText(spannable, TextView.BufferType.SPANNABLE)
    }

    fun setSizes(
        view: TextView, textStart: String, testEnd: String,
        sizeRangeStart: Float = 1.0F, sizeRangeEnd: Float = 0.5F
    ) {
        val spannable = SpannableString("$textStart$testEnd")
        spannable.setSpan(
            RelativeSizeSpan(sizeRangeStart),
            0,
            textStart.length,
            0
        )
        spannable.setSpan(
            RelativeSizeSpan(sizeRangeEnd),
            textStart.length,
            textStart.length + testEnd.length,
            0
        )
        view.setText(spannable, TextView.BufferType.SPANNABLE)
    }

    fun setSizedColorText(
        view: TextView,
        textStart: String,
        textEnd: String,
        colorStart: Int,
        colorEnd: Int,
        sizeRangeStart: Float,
        sizeRangeEnd: Float,
        fontStart: Int,
        fontEnd: Int
    ) {
        val spannable = SpannableString("$textStart$textEnd")

        spannable.setSpan(ForegroundColorSpan(getColorStart(textEnd, colorStart, colorEnd)), 0, textStart.length, 0)
        spannable.setSpan(CustomTypefaceSpan(view.context, fontStart), 0, textStart.length, 0)

        spannable.setSpan(ForegroundColorSpan(colorEnd), textStart.length, textStart.length + textEnd.length, 0)
        spannable.setSpan(CustomTypefaceSpan(view.context, fontEnd), textStart.length, textStart.length + textEnd.length, 0)
        spannable.setSpan(
            RelativeSizeSpan(sizeRangeStart),
            0,
            textStart.length,
            0
        )
        spannable.setSpan(
            RelativeSizeSpan(sizeRangeEnd),
            textStart.length,
            textStart.length + textEnd.length,
            0
        )
        view.setText(spannable, TextView.BufferType.SPANNABLE)
    }

    fun setPartialClickable(textView: TextView, text: String, end: String, onClick: () -> Unit) {
        val sString = SpannableString("$text $end")
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                onClick()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        sString.setSpan(clickableSpan, text.length + 1, (text.length + end.length) + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sString.setSpan(UnderlineSpan(), text.length + 1, (text.length + end.length) + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = sString
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.highlightColor = Color.TRANSPARENT
    }
}