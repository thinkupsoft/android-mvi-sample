package com.thinkup.models.services

import com.google.gson.annotations.SerializedName
import com.thinkup.models.app.Category

data class CategoriesResponse(
    @SerializedName("categories") val categories: List<Category>
)