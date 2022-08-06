package com.thinkup.mvi.utils

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.provider.OpenableColumns
import android.text.format.DateFormat
import androidx.annotation.StringRes
import com.thinkup.common.orZero
import com.thinkup.common.random
import com.thinkup.common.ui.DateUtils
import com.thinkup.common.ui.FileUtil
import com.thinkup.mvi.R
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

class ResourcesHelper @Inject constructor(
    @ApplicationContext val context: Context
) {

    fun isValidDate(input: String): Date? = try {
        val dateUtils = DateUtils()
        val finalFormat = getDateFormat()
        dateUtils.parse(input, finalFormat.replace("/", "").replace(".", ""))
    } catch (ex: Exception) {
        null
    }

    fun getDateFormat(): String {
        val formatter = DateFormat.getDateFormat(context) as SimpleDateFormat
        return when (val finalFormat = formatter.toPattern()) {
            "M/d/yy" -> "MM/dd/yyyy"
            "d/M/y" -> "dd/MM/yyyy"
            "dd/MM/yy" -> "dd/MM/yyyy"
            "dd.MM.yy" -> "dd.MM.yyyy"
            else -> finalFormat
        }
    }

    fun formatServerDate(input: String) = try {
        val dateUtils = DateUtils()
        val finalFormat = getDateFormat()
        dateUtils.format(dateUtils.parse(input, DateUtils.FORMAT_DATE1), finalFormat)
    } catch (ex: Exception) {
        getString(R.string.no_data)
    }

    fun getDateMask(): String {
        val format = getDateFormat()
        return format.map { if (it.isLetter()) "#" else it }.joinToString("")
    }

    fun getString(@StringRes res: Int) = context.getString(res)

    fun getFileTempDir() = File(context.cacheDir, "avatar-temp.jpg")

    fun getFile(uri: Uri): File {
        val newName = String.random()
        val extension = ".${getExt(uri)}"
        val inputStream = context.contentResolver?.openInputStream(uri)
        return inputStream?.let {
            val file =
                File.createTempFile(newName, extension, context.filesDir)
            file.outputStream().use { fileOut ->
                inputStream.copyTo(fileOut)
            }
            file
        } ?: run { FileUtil.getFile(context) }
    }

    private fun getExt(uri: Uri): String {
        val cursor: Cursor? =
            context.contentResolver?.query(uri, null, null, null, null)
        val nameIndex: Int = cursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME).orZero()
        cursor?.moveToFirst()
        val name = cursor?.getString(nameIndex).orEmpty()
        val extension = name.split(".").last()
        cursor?.close()
        return extension
    }

    fun saveBitmap(path: String?, bitmap: Bitmap?): File {
        // help for fix landscape photos
        val outStream: OutputStream?
        var file = File(path)
        if (file.exists()) {
            file.delete()
            file = File(path)
        }
        try { // make a new bitmap from your file
            outStream = FileOutputStream(path)
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
            outStream.flush()
            outStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return file
    }
}