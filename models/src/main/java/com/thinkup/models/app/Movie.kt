package com.thinkup.models.app

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class Movie(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("subtitle") val subtitle: String?,
    @SerializedName("actors") val actors: String,
    @SerializedName("director") val director: String?,
    @SerializedName("publication_date") val publicationDate: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("active") val active: Boolean,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("images") val images: List<MovieImage>,
    @SerializedName("categories") val categories: List<Category>
): Parcelable, Serializable

@Parcelize
data class MovieImage(
    @SerializedName("image_url") val imageUrl: String
): Parcelable, Serializable