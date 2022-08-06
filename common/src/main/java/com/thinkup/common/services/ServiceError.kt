package com.thinkup.common.services

import com.google.gson.annotations.SerializedName
import com.thinkup.common.empty

class ServiceError constructor(
    @SerializedName("code") val code: String,
    @SerializedName("msg") val msg: String?,
    @SerializedName("errors") val errors: Errors?,
    var url: String = ""
) {
    fun getMessage() = msg ?: errors?.errors?.map { it.msg }?.joinToString("\n").orEmpty()
}

data class Errors(@SerializedName("errors") val errors: List<ApiError>?)

data class ApiError(
    @SerializedName("value") val value: String = String.empty(),
    @SerializedName("msg") val msg: String = String.empty(),
    @SerializedName("param") val param: String = String.empty(),
    @SerializedName("location") val body: String = String.empty()
)