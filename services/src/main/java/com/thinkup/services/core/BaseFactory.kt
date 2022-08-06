package com.thinkup.services.core

import android.content.Context
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.thinkup.services.BuildConfig
import com.thinkup.services.api.CacheInfo
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

abstract class BaseFactory(protected val context: Context) {

    abstract fun <T> createInstance(clazz: Class<T>): T

    /**
     * Create retrofit instance
     */
    protected fun retrofit(apiUrl: String, okHttpClient: OkHttpClient) = Retrofit.Builder()
        .baseUrl(apiUrl)
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory())
        .build()

    abstract fun okHttpClient(context: Context, cacheInfo: CacheInfo?, hasRefreshService: Boolean, shouldUseMock: Boolean): OkHttpClient

    /**
     * Create logging interceptor instance
     */
    protected fun addLogging(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        // Add the logging interceptor if it is a debug build
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)
        }
        return builder
    }

    /**
     * Create a Gson instance
     * Used to convert Json to classes and vice versa
     */
    private fun gsonConverterFactory(): GsonConverterFactory {
        val gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()

        return GsonConverterFactory.create(gson)
    }
}