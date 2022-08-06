package com.thinkup.services.services

import com.thinkup.models.services.ForgotPasswordRequest
import com.thinkup.models.services.LoginRequest
import com.thinkup.models.services.LogoutRequest
import com.thinkup.models.services.RedeemPasswordRequest
import com.thinkup.models.services.RegisterRequest
import com.thinkup.models.services.UserDataResponse
import com.thinkup.services.BuildConfig
import com.thinkup.services.api.ApiUrl
import com.thinkup.services.api.CacheInfo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

@CacheInfo
@ApiUrl(BuildConfig.API_URL, hasRefreshService = false, shouldUseMock = true)
interface AuthService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<UserDataResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<UserDataResponse>

    @POST("auth/logout")
    suspend fun logout(@Body request: LogoutRequest): Response<Any>

    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): Response<Any>

    @PUT("auth/forgot-password")
    suspend fun redeemPassword(@Body request: RedeemPasswordRequest): Response<Any>
}