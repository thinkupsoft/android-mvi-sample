package com.thinkup.services.interceptors

import com.thinkup.services.core.NetworkUtils
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Check connectivity before each request
 */
internal class ConnectivityInterceptor(private val networkUtils: NetworkUtils) : BaseInterceptor() {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isConnected()) {
            throw IOException("No internet connection")
        }
        return chain.proceed(chain.request())
    }

    @Suppress("DEPRECATION")
    private fun isConnected(): Boolean = networkUtils.isConnected()

}