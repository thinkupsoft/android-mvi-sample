package com.thinkup.common.ui

import android.content.Context
import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.MetricAffectingSpan
import androidx.annotation.FontRes
import androidx.core.content.res.ResourcesCompat

class CustomTypefaceSpan(context: Context, @FontRes fontId: Int) : MetricAffectingSpan() {

    private val typeface: Typeface? = getTypeface(context, fontId)

    override fun updateDrawState(paint: TextPaint) {
        paint.typeface = typeface
    }

    override fun updateMeasureState(paint: TextPaint) {
        paint.typeface = typeface
    }

    private fun getTypeface(context: Context, @FontRes fontId: Int): Typeface? {
        return ResourcesCompat.getFont(context, fontId)
    }
}