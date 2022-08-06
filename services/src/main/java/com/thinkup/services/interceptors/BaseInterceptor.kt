package com.thinkup.services.interceptors

import com.thinkup.common.AUTH_TOKEN
import okhttp3.Interceptor

abstract class BaseInterceptor : Interceptor {
    companion object {
        const val REFRESH_TIMEOUT = 2000
        const val AUTHORIZATION = "Authorization"
        const val ACCEPT_LANGUAGE = "Accept-Language"
        const val CONTENT_TYPE = "Content-Type"
        const val CONTENT_TYPE_VALUE = "application/json; charset=utf-8"
        const val BEARER = "Bearer "
        const val LOGOUT = "logout"
        const val CACHE_CONTROL = "Cache-Control"
        const val CACHE_PUBLIC = "public, "
        const val GET_METHOD = "GET"
    }

    protected fun getTokenType(): String {
        return AUTH_TOKEN
    }
}