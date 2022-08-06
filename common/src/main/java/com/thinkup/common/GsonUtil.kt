package com.thinkup.common

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.lang.reflect.Type

object GsonUtil {
    fun <T> fromBuffer(reader: BufferedReader, type: Type) = Gson().fromJson<T>(reader, type)
    fun <T> toJson(item: T): String = Gson().toJson(item)
    fun <T : Any> fromJson(item: String, type: Type): T = Gson().fromJson(item, type)
    inline fun <reified T> typed() = TypeToken.getParameterized(List::class.java, T::class.java)
}