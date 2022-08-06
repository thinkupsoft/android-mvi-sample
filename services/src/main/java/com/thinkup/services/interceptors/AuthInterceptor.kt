package com.thinkup.services.interceptors

import android.content.Context
import com.thinkup.common.AUTH_TOKEN
import com.thinkup.common.AUTH_TOKEN_KEY
import com.thinkup.common.REFRESH_TOKEN
import com.thinkup.common.REFRESH_TOKEN_KEY
import com.thinkup.common.USER_TOKEN_KEY
import com.thinkup.common.preferences.KeystoreManager

import okhttp3.Interceptor
import okhttp3.Response
import java.util.Locale

/**
 * If the app have a authToken saved use ot to put in a header for each request
 */
class AuthInterceptor(context: Context) : BaseInterceptor() {

    private val keystoreManager = KeystoreManager(context)

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val token = when (getTokenType()) {
            AUTH_TOKEN -> getAuthToken()
            REFRESH_TOKEN -> getRefreshToken()
            else -> null
        }

        // Only used internally
        var requestBuilder = request.newBuilder().removeHeader(USER_TOKEN_KEY)

        // Add authorization
        if (token != null) {
            requestBuilder = requestBuilder.addHeader(AUTHORIZATION, token)
        }
        requestBuilder = requestBuilder.addHeader(ACCEPT_LANGUAGE, Locale.getDefault().language)

        return chain.proceed(requestBuilder.build())
    }

    private fun getAuthToken(): String? {
        val token = keystoreManager.getValue(AUTH_TOKEN_KEY)
        return "$BEARER$token"
    }

    private fun getRefreshToken(): String? {
        val token = keystoreManager.getValue(REFRESH_TOKEN_KEY)
        return "$BEARER$token"
    }
}