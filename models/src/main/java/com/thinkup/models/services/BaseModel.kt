package com.thinkup.models.services

import com.google.gson.annotations.SerializedName
import com.thinkup.models.app.User

data class UserDataResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("user") val user: User
)