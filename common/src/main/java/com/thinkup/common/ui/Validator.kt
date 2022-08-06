package com.thinkup.common.ui

import android.util.Patterns
import java.util.regex.Pattern

class Validator {

    fun isWeb(source: String?): Boolean {
        return source?.let { Patterns.WEB_URL.matcher(source).matches() } ?: run { false }
    }

    fun isEmail(source: String?): Boolean {
        return source?.let {
            val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
            val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
            val matcher = pattern.matcher(source)
            matcher.matches()
        } ?: false
    }
}