package com.thinkup.services.services

import com.thinkup.models.app.User
import com.thinkup.models.services.ChangePasswordRequest
import com.thinkup.models.services.EditProfileRequest
import com.thinkup.services.BuildConfig
import com.thinkup.services.api.ApiUrl
import com.thinkup.services.api.CacheInfo
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part

@CacheInfo
@ApiUrl(BuildConfig.API_URL, hasRefreshService = true, shouldUseMock = true)
interface UserService {

    @Multipart
    @PUT("users/image")
    suspend fun updateAvatar(@Part file: MultipartBody.Part): Response<User>

    @PUT("users/update")
    suspend fun updateProfile(@Body request: EditProfileRequest): Response<Any>

    @DELETE("users/delete")
    suspend fun deleteAccount(): Response<Any>

    @PUT("users/change-password")
    suspend fun changePassword(@Body request: ChangePasswordRequest): Response<Any>
}