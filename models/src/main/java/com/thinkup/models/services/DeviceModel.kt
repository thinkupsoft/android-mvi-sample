package com.thinkup.models.services

import com.google.gson.annotations.SerializedName

data class DeviceRequest(
    @SerializedName("fcm_token") val fcmToken: String
)