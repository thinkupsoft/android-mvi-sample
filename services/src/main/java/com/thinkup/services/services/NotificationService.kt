package com.thinkup.services.services

import com.thinkup.models.services.DeviceRequest
import com.thinkup.services.BuildConfig
import com.thinkup.services.api.ApiUrl
import com.thinkup.services.api.CacheInfo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HTTP
import retrofit2.http.POST

@CacheInfo
@ApiUrl(BuildConfig.API_URL, hasRefreshService = true, shouldUseMock = true)
interface NotificationService {

    @POST("notifications/add")
    suspend fun addDevice(@Body request: DeviceRequest): Response<Any>

    @HTTP(method = "DELETE", path = "notifications/remove", hasBody = true)
    suspend fun deleteDevice(@Body request: DeviceRequest): Response<Any>
}