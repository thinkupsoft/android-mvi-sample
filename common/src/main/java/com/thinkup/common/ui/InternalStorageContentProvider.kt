package com.thinkup.common.ui

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.ParcelFileDescriptor
import com.thinkup.common.IMAGE_JPEG
import com.thinkup.common.JPEG
import com.thinkup.common.JPG
import java.io.File
import java.io.FileNotFoundException

const val CONTENT_URI_FACTORY = "content://com.thinkup.merkos/"

class InternalStorageContentProvider() : ContentProvider() {

    companion object {
        const val TEMP_PHOTO_FILE_NAME = "temp_photo.jpg"
        private val MIME_TYPES = hashMapOf(Pair(JPG, IMAGE_JPEG), Pair(JPEG, IMAGE_JPEG))
    }

    override fun onCreate(): Boolean {
        return try {
            val mFile = File(context!!.filesDir, TEMP_PHOTO_FILE_NAME)
            if (!mFile.exists()) {
                mFile.createNewFile()
                context!!.contentResolver.notifyChange(Uri.parse(CONTENT_URI_FACTORY), null)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
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
        val f = File(context!!.filesDir, TEMP_PHOTO_FILE_NAME)
        if (f.exists()) {
            return ParcelFileDescriptor.open(f, ParcelFileDescriptor.MODE_READ_WRITE)
        }
        throw FileNotFoundException(uri.path)
    }

    override fun getType(p0: Uri): String? {
        val path = p0.toString()
        for (extension in MIME_TYPES.keys) {
            if (path.endsWith(extension)) {
                return MIME_TYPES[extension]
            }
        }
        return null
    }
}