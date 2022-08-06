package com.thinkup.services.services

import com.thinkup.models.app.Actor
import com.thinkup.models.app.Director
import com.thinkup.models.app.Movie
import com.thinkup.models.services.PagingResponse
import com.thinkup.models.services.SortType
import com.thinkup.services.BuildConfig
import com.thinkup.services.api.ApiUrl
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

@ApiUrl(BuildConfig.API_URL, hasRefreshService = true, shouldUseMock = true)
interface MovieService {

    @Multipart
    @POST("movie/add")
    suspend fun addMovie(
        @Part("title") title: RequestBody,
        @Part("subtitle") subtitle: RequestBody?,
        @Part("actors") actors: RequestBody,
        @Part("director") director: RequestBody?,
        @Part("publication_date") publicationDate: RequestBody?,
        @Part("categories") categories: RequestBody,
        @Part("description") description: RequestBody?,
        @Part files: List<MultipartBody.Part>
    ): Response<Movie>

    @GET("movie/user")
    suspend fun getMyMovies(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Response<PagingResponse<Movie>>

    @GET("movie/actors")
    suspend fun getActors(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Response<PagingResponse<Actor>>

    @GET("movie/directors")
    suspend fun getDirectors(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Response<PagingResponse<Director>>

    @GET("movie/featured")
    suspend fun getFeaturedMovies(
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 5,
        @Query("sort") sort: String = SortType.RANDOM.value,
    ): Response<PagingResponse<Movie>>
}