package com.thinkup.services.core

import android.content.Context
import com.thinkup.services.BuildConfig
import com.thinkup.services.api.CacheInfo
import com.thinkup.services.api.RefreshService
import com.thinkup.services.interceptors.AuthInterceptor
import okhttp3.OkHttpClient

/**
 * Internal module class
 * Used for create a [RefreshService] instance
 */
class RefreshFactory(context: Context) : BaseFactory(context) {
    private val baseUrl = BuildConfig.API_URL

    override fun <T> createInstance(clazz: Class<T>): T {
        return retrofit(
            apiUrl = baseUrl,
            okHttpClient = okHttpClient(context, null, false, false)
        ).create(clazz)
    }

    override fun okHttpClient(
        context: Context,
        cacheInfo: CacheInfo?,
        hasRefreshService: Boolean,
        shouldUseMock: Boolean
    ): OkHttpClient {
        var clientBuilder = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
        clientBuilder = addLogging(clientBuilder)
        return clientBuilder.build()
    }
}