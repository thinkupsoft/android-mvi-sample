package com.thinkup.services.interceptors

import com.thinkup.services.api.CacheInfo
import com.thinkup.services.core.CustomCache
import com.thinkup.services.core.NetworkUtils
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.IOException
import java.util.concurrent.TimeUnit

internal class CacheInterceptor(
    private val networkUtils: NetworkUtils,
    private val postCache: CustomCache,
    private val info: CacheInfo?
) : BaseInterceptor() {

    companion object {
        const val CACHE_EXCEPTION_MESSAGE = "you forgot to add the CacheInfo annotation"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        if (info == null) {
            ensureInternetConnection()
            return chain.proceed(chain.request())
        }
        return if (chain.request().method != GET_METHOD) {
            postCache(chain)
        } else {
            if (!networkUtils.isConnected()) {
                val request = cacheOffline(chain)
                chain.proceed(request)
            } else {
                val response = chain.proceed(chain.request())
                val cache = cacheOnline()
                response.newBuilder()
                    .header(CACHE_CONTROL, "$CACHE_PUBLIC$cache")
                    .build()
            }
        }
    }

    private fun cacheOnline(): CacheControl {
        val cacheControl = CacheControl.Builder()
        val time = info?.lifeTimeCache ?: 0
        if (time == 0) {
            cacheControl.noCache()
        } else {
            cacheControl.maxAge(time, TimeUnit.SECONDS)
        }
        return cacheControl.build()
    }

    private fun cacheOffline(chain: Interceptor.Chain): Request {
        val request = chain.request()
        val cacheControl = CacheControl.Builder()
            .maxStale((info?.lifeTimeOffline ?: 0) * 24 * 60 * 60, TimeUnit.SECONDS)
            .onlyIfCached()
            .build()

        return request.newBuilder()
            .cacheControl(cacheControl)
            .build()
    }

    private fun postCache(chain: Interceptor.Chain): Response {
        return postCache(chain, getTime())
    }

    private fun getTime(): Int {
        return if (!networkUtils.isConnected()) {
            (info?.lifeTimeOffline ?: 0) * 24 * 60 * 60
        } else {
            (info?.lifeTimeCache ?: 0)
        }
    }

    private fun postCache(chain: Interceptor.Chain, maxTime: Int): Response {
        val body = postCache.read(chain.request(), maxTime)
        body?.let {
            return Response.Builder().request(chain.request())
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("")
                .body(body.toResponseBody(CONTENT_TYPE_VALUE.toMediaType()))
                .build()
        } ?: run {
            ensureInternetConnection()
            val response = chain.proceed(chain.request())
            postCache.write(chain.request(), response)
            return response
        }
    }

    private fun ensureInternetConnection() {
        if (!networkUtils.isConnected()) {
            throw IOException("No internet connection")
        }
    }

    internal class CacheException : Exception(CACHE_EXCEPTION_MESSAGE)
}