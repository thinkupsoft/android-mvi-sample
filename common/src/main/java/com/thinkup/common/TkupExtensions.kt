package com.thinkup.common

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Patterns
import android.view.View
import android.view.ViewGroup
import okhttp3.MediaType
import okhttp3.RequestBody

const val PASSWORD_POLICY = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%!\\-_?&])(?=\\S+\$).{8,}"

fun Int.Companion.ZERO_VALUE() = 0
fun Int?.orZero() = this ?: 0
fun Double?.orZero() = this ?: 0.0
fun Int?.toBoolean() = this?.let { it > 0 } ?: false
fun Int.notNegative() = if (this > 0) this else 0

fun String.Companion.empty() = ""
fun String.Companion.whitespace() = " "
fun String?.isEmail() = Patterns.EMAIL_ADDRESS.matcher(this.orEmpty()).matches()
fun String?.isValidPassword() = PASSWORD_POLICY.toRegex().matches(this.orEmpty())
fun String.Companion.random(length: Int = 6, allowDigits: Boolean = true): String {
    val charPool: List<Char> = if (allowDigits) ('a'..'z') + ('A'..'Z') + ('0'..'9')
    else ('a'..'z') + ('A'..'Z')
    return (1..length)
        .map { kotlin.random.Random.nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("");
}

fun String.toRequestBody(): RequestBody = RequestBody.create(MediaType.parse("text/plain"), this)
fun String?.tryRequestBody(): RequestBody? {
    return this?.let {
        RequestBody.create(MediaType.parse("text/plain"), it)
    } ?: run { null }
}

fun ViewGroup.getActivity(): Activity? {
    var context = context
    while (context is ContextWrapper) {
        if (context is Activity) {
            return context
        }
        context = context.baseContext
    }
    return null
}

fun View.getActivity(): Activity? {
    var context = context
    while (context is ContextWrapper) {
        if (context is Activity) {
            return context
        }
        context = context.baseContext
    }
    return null
}

fun Context.getActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) {
            return context
        }
        context = context.baseContext
    }
    return null
}