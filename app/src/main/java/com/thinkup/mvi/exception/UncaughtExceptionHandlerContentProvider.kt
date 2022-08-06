package com.thinkup.mvi.exception

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.ParcelFileDescriptor

class UncaughtExceptionHandlerContentProvider : ContentProvider() {

    override fun onCreate(): Boolean {
        setUpCrashHandling()
        return true
    }

    private fun setUpCrashHandling() {
        Thread.setDefaultUncaughtExceptionHandler { _, _ -> /* do nothing to avoid crashlytics kill the app */ }
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        return 0
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        return 0
    }

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        return null
    }

    override fun query(
        p0: Uri,
        p1: Array<out String>?,
        p2: String?,
        p3: Array<out String>?,
        p4: String?
    ): Cursor? {
        return null
    }

    override fun openFile(uri: Uri, mode: String): ParcelFileDescriptor? {
        return null
    }

    override fun getType(p0: Uri): String? {
        return null
    }
}