package com.thinkup.common.services

import com.google.gson.Gson
import retrofit2.Response
import java.net.HttpURLConnection

class ServiceException private constructor() : Exception() {
    companion object {
        fun <T> factory(response: Response<T>): ServiceException {
            val url = response.toString().split("url=")[1].replace("}", "")
            return try {
                if (response.code() == HttpURLConnection.HTTP_GATEWAY_TIMEOUT) {
                    ServiceException().apply { error = getInternetError() }
                } else {
                    val parse = Gson().fromJson(response.errorBody()?.string(), ServiceError::class.java)
                    ServiceException().apply {
                        error = parse ?: getError(
                            response.code().toString(),
                            response.message(),
                            url
                        )
                        error?.url = url
                    }
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                ServiceException().apply {
                    error =
                        getError(
                            response.code().toString(),
                            ex.message.orEmpty(),
                            url
                        )
                }
            }
        }

        private fun getError(code: String?, message: String?, url: String) =
            ServiceError(code ?: "500", message, Errors(listOf(ApiError(msg = message.orEmpty()))), url)

        private fun getInternalError(message: String?) =
            ServiceError("101", message, Errors(listOf(ApiError(msg = message.orEmpty()))))

        private fun getInternetError() =
            ServiceError("504", "No internet connection", Errors(listOf(ApiError(msg = "No internet connection"))))
    }

    var error: ServiceError? = null
        internal set

    override fun toString(): String = "${error?.code ?: 500}: ${error?.getMessage()}"
}