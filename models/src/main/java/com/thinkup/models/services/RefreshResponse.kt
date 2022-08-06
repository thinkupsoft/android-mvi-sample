package com.thinkup.models.services

import com.google.gson.annotations.SerializedName

/**
 * Use @SerializedName to match json key names with your model var name
 * Whenever possible have your data class models
 */
data class RefreshResponse(
    @SerializedName("access_token") val token: String
)

data class RefreshRequest(
    @SerializedName("refresh_token") val refreshToken: String
)