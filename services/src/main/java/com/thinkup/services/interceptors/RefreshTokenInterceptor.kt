package com.thinkup.services.interceptors

import android.content.Context
import com.thinkup.common.AUTH_TOKEN_KEY
import com.thinkup.common.REFRESH_TOKEN_KEY
import com.thinkup.common.preferences.KeystoreManager
import com.thinkup.common.services.check
import com.thinkup.models.services.RefreshRequest
import com.thinkup.models.services.RefreshResponse
import com.thinkup.services.api.RefreshService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.net.HttpURLConnection
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * if a response is forbidden (401), call refresh service, wait for a response and update the authToken
 * reprocess the forbidden reuqest with the new token
 */
internal class RefreshTokenInterceptor(
    context: Context,
    private val userService: RefreshService
) : BaseInterceptor() {

    private val keystoreManager = KeystoreManager(context)

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val response = chain.proceed(originalRequest)

        if (!response.isSuccessful) {
            val tokenType = getTokenType()
            if (isForbidden(response.code) && tokenType.isNotEmpty()) {
                val requestWithNewAuthHeader = recreateAuthToken(chain, originalRequest)
                response.close()
                return chain.proceed(requestWithNewAuthHeader)
            }
        }
        return response
    }

    private fun generateNewToken(): String? {
        var newApiToken = ""
        val refreshTokenReady = CountDownLatch(1)

        CoroutineScope(Dispatchers.Default).launch {
            val refreshToken = keystoreManager.getValue(REFRESH_TOKEN_KEY).orEmpty()
            newApiToken = success(userService.refresh(RefreshRequest(refreshToken))) ?: ""
        }
        try {
            refreshTokenReady.await(REFRESH_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        return newApiToken
    }

    private suspend fun success(result: retrofit2.Response<RefreshResponse>): String? {
        var apiToken: String? = null
        try {
            apiToken = result.check().token
            keystoreManager.setValue(AUTH_TOKEN_KEY, apiToken)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return apiToken
    }

    private fun recreateAuthToken(chain: Interceptor.Chain, originalRequest: Request): Request {
        val newToken = generateNewToken()
        if (!newToken.isNullOrEmpty()) {
            var newRequest = chain.request()
            newRequest = newRequest.newBuilder()
                .header(AUTHORIZATION, "$BEARER$newToken")
                .header(CONTENT_TYPE, CONTENT_TYPE_VALUE)
                .method(originalRequest.method, originalRequest.body)
                .build()
            return newRequest
        }
        return originalRequest
    }

    private fun isForbidden(code: Int): Boolean {
        return code == HttpURLConnection.HTTP_FORBIDDEN || code == HttpURLConnection.HTTP_UNAUTHORIZED
    }
}
