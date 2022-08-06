package com.thinkup.common.ui

import android.util.DisplayMetrics

object ItemUtils {
    fun containerWidth(displayMetrics: DisplayMetrics, percentageWidth: Float, margins: Int = 0): Int {
        return ((displayMetrics.widthPixels - margins) * percentageWidth).toInt() / 100
    }
}