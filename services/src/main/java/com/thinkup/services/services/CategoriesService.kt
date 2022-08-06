package com.thinkup.services.services

import com.thinkup.models.services.CategoriesResponse
import com.thinkup.services.BuildConfig
import com.thinkup.services.api.ApiUrl
import com.thinkup.services.api.CacheInfo
import retrofit2.Response
import retrofit2.http.GET

@CacheInfo(lifeTimeCache = 5 * 60, lifeTimeOffline = 5, lifeTimeDatabase = 5 * 60)
@ApiUrl(BuildConfig.API_URL, hasRefreshService = true, shouldUseMock = true)
interface CategoriesService {

    @GET("movie/categories")
    suspend fun getCategories(): Response<CategoriesResponse>
}