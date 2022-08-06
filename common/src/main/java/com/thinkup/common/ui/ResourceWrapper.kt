package com.thinkup.common.ui

import android.content.Context
import android.net.Uri
import androidx.annotation.DimenRes
import androidx.annotation.StringRes

/**
 * Encapsulate the access to R values from the viewmodel
 * Use this to get a string, dimen or any resource value outside the ui classes
 */
class ResourceWrapper(private val context: Context) {

    fun getString(@StringRes resource: Int): String = context.getString(resource)

    fun getString(@StringRes resource: Int, params: String): String = context.getString(resource, params)

    fun getDimen(@DimenRes resource: Int) = context.resources.getDimensionPixelSize(resource)

    fun getPath(uri: Uri) = FilePath.getPath(context, uri)
}