package com.thinkup.models.app

import com.google.gson.annotations.SerializedName

data class Actor(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: String
)