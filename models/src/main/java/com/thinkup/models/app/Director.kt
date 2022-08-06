package com.thinkup.models.app

import com.google.gson.annotations.SerializedName

data class Director(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: String
)