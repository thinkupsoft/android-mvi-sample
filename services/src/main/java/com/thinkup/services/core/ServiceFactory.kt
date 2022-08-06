package com.thinkup.services.core

import android.content.Context
import com.thinkup.services.BuildConfig
import com.thinkup.services.api.ApiUrl
import com.thinkup.services.api.CacheInfo
import com.thinkup.services.api.RefreshService
import com.thinkup.services.interceptors.AuthInterceptor
import com.thinkup.services.interceptors.CacheInterceptor
import com.thinkup.services.interceptors.MockInterceptor
import com.thinkup.services.interceptors.RefreshTokenInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.io.File

/**
 * Use this to create retrofit instance
 * @param hasRefreshService: if your backend use a token to identify and have an endpoint to refresh it, pass true
 */
class ServiceFactory(
    context: Context,
    private val postCache: CustomCache
) : BaseFactory(context) {
    private val defaultApiUrl = BuildConfig.API_URL
    private val refreshFactory = RefreshFactory(context)

    /**
     * Get an instance for an specific retrofit interfaces
     * take the url from the interface annotation or default if it dont have annotation
     * @sample: [RefreshService]
     */
    override fun <T> createInstance(clazz: Class<T>): T {
        val apiUrlAnnotation = clazz.annotations.find { it is ApiUrl } as ApiUrl?
        val cacheInfoAnnotation = clazz.annotations.find { it is CacheInfo } as CacheInfo?

        val url = apiUrlAnnotation?.url ?: defaultApiUrl
        val hasRefreshService: Boolean = apiUrlAnnotation?.hasRefreshService ?: true
        val shouldUseMock: Boolean = apiUrlAnnotation?.shouldUseMock ?: false

        return retrofit(
            apiUrl = url,
            okHttpClient = okHttpClient(context, cacheInfoAnnotation, hasRefreshService, shouldUseMock)
        ).create(clazz)
    }

    /**
     * Get an [okHttpClient] with customs interceptors and logging to console
     */
    override fun okHttpClient(
        context: Context,
        cacheInfo: CacheInfo?,
        hasRefreshService: Boolean,
        shouldUseMock: Boolean
    ): OkHttpClient {
        val networkUtils = NetworkUtils(context)
        val cacheSize = 10 * 1024 * 1024 // 10 MiB
        var clientBuilder = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .addInterceptor(CacheInterceptor(networkUtils, postCache, cacheInfo))
            .cache(Cache(File(context.cacheDir, "responses"), cacheSize.toLong()))

        if (hasRefreshService)
            clientBuilder.addInterceptor(
                RefreshTokenInterceptor(
                    context,
                    refreshFactory.createInstance(
                        RefreshService::class.java
                    )
                )
            )
        clientBuilder = addMockInterceptor(clientBuilder, context, shouldUseMock)
        clientBuilder = addLogging(clientBuilder)
        return clientBuilder.build()
    }

    /**
     * Create mock interceptor instance
     */
    private fun addMockInterceptor(builder: OkHttpClient.Builder, context: Context, shouldUseMock: Boolean): OkHttpClient.Builder {
        // Add the mock interceptor if it is a debug build
        if (BuildConfig.DEBUG && shouldUseMock) {
            val mockInterceptor = MockInterceptor(context)
            builder.addInterceptor(mockInterceptor)
        }
        return builder
    }
}