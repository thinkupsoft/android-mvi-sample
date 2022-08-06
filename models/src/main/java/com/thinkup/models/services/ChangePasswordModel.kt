package com.thinkup.models.services

import com.google.gson.annotations.SerializedName

data class ChangePasswordRequest(
    @SerializedName("old_password") val oldPassword: String,
    @SerializedName("new_password") val newPassword: String
)