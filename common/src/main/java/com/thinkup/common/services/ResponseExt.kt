package com.thinkup.common.services

import retrofit2.Response
import java.net.HttpURLConnection

fun <T> Response<T>.check(): T {
    if (isSuccessful) {
        body()?.let {
            return it
        }
    } else if (code() == HttpURLConnection.HTTP_FORBIDDEN || code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
        throw SessionException()
    } else if (code() == HttpURLConnection.HTTP_UNAVAILABLE) {
        throw UnavailableException()
    }
    throw ServiceException.factory(this)
}

fun <T> Response<T>.softCheck(): Response<T> {
    if (isSuccessful) {
        return this
    } else if (code() == HttpURLConnection.HTTP_FORBIDDEN || code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
        throw SessionException()
    } else if (code() == HttpURLConnection.HTTP_UNAVAILABLE) {
        throw UnavailableException()
    }
    throw ServiceException.factory(this)
}