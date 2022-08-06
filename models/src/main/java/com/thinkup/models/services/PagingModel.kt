package com.thinkup.models.services

import com.google.gson.annotations.SerializedName

data class PagingResponse<T>(
    @SerializedName("total_items") val totalItems: Int,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("current_page") val currentPage: Int,
    @SerializedName("per_page") val perPage: Int,
    @SerializedName("offset") val offset: Int,
    @SerializedName("data") val data: List<T>
)

enum class SortType(val value: String) {
    NEWER("NEWER"),
    OLDER("OLDER"),
    TITLE("TITLE"),
    AUTHOR("AUTHOR"),
    RANDOM("RANDOM"),
    ID_DESC("ID_DESC")
}