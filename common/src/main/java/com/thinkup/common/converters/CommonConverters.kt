package com.thinkup.common.converters

import androidx.room.TypeConverter
import com.thinkup.common.GsonUtil
import java.util.*

class CommonConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun listFromString(value: String?): List<String>? {
        val list =  GsonUtil.typed<String>()
        return value?.let { GsonUtil.fromJson(value, list.type) }
    }

    @TypeConverter
    fun listToString(list: List<String>?): String? {
        return GsonUtil.toJson(list)
    }
}