package com.thinkup.models.services

import com.google.gson.annotations.SerializedName

data class EditProfileRequest(
    @SerializedName("name") val name: String,
    @SerializedName("username") val username: String
)