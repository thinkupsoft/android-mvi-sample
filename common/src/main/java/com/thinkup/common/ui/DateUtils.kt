package com.thinkup.common.ui

import java.text.SimpleDateFormat
import java.util.*

class DateUtils {

    fun format(date: Date, format: String) = apply(date, format)

    fun format(timestamp: Long, format: String) = apply(Date(timestamp), format)

    fun now(format: String) = apply(Calendar.getInstance().time, format)

    fun now() = Calendar.getInstance().time

    fun parse(timestamp: Long) = Date(timestamp)

    fun parse(source: String, format: String) = SimpleDateFormat(format, Locale.getDefault()).parse(source)

    private fun apply(date: Date, format: String): String {
        val simpleDateFormatNew = SimpleDateFormat(format, Locale.getDefault())
        return simpleDateFormatNew.format(date)
    }

    companion object {
        const val FORMAT_TIME1 = "HH:mm"
        const val FORMAT_TIME2 = "HH:mm:ss"
        const val FORMAT_DATE1 = "yyyy-MM-dd"
        const val FORMAT_DATE2 = "dd/MM/yyyy"
        const val FORMAT_DATE3 = "dd/MM/yyyy HH:mm"
        const val FORMAT_DATE4 = "dd/MM/yyyy HH:mm:ss"
        const val FORMAT_DATE5 = "yyyy-MM-dd HH:mm"
        const val FORMAT_DATE6 = "yyyy-MM-dd HH:mm:ss"
        const val FORMAT_DATE7 = "yyyy-MM-dd'T'HH:mm:ss"
    }
}