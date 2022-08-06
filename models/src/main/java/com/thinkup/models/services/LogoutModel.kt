package com.thinkup.models.services

import com.google.gson.annotations.SerializedName

data class LogoutRequest(
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("fcm_token") val fcmToken: String
)