package com.thinkup.services.interceptors

import android.content.Context
import com.thinkup.services.BuildConfig
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException
import java.net.URI

/**
 * If the app have a mock json to test your service
 * @sample How it work?
 * - Add a json to assets. ex: assets/v1/users/info.json
 * - In the DiWrapper, mark your service with the falg 'shouldUseMock' in true
 * - define your service with the same url as the path. ex: @GET('v1/users/info.json')
 */
class MockInterceptor(private val context: Context) : BaseInterceptor() {

    override fun intercept(chain: Interceptor.Chain): Response {
        var response: Response? = null
        if (BuildConfig.DEBUG) {
            // Get Request URI.
            val uri: URI = chain.request().url.toUri()
            // Get Query String.
            var path: String = uri.path

            path = if (path.startsWith('/')) path.substring(1) else path

            val responseString: String = getJsonDataFromAsset(context, path).orEmpty()

            response = Response.Builder()
                .code(200)
                .message(responseString)
                .request(chain.request())
                .protocol(Protocol.HTTP_1_0)
                .body(ResponseBody.create("application/json".toMediaType(), responseString.toByteArray()))
                .addHeader("content-type", "application/json")
                .build()
        } else {
            response = chain.proceed(chain.request())
        }

        return response
    }

    fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open("$fileName.json").bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }
}