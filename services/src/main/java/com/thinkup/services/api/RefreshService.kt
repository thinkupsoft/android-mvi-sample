package com.thinkup.services.api

import com.thinkup.models.services.RefreshRequest
import com.thinkup.models.services.RefreshResponse
import com.thinkup.services.BuildConfig
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Annotate your Service interface with @ApiUrl to indicate the base url for the class call functions
 * Annotate your endpoint function with the correct request method(GET, POST, PUT, etc)
 * Make your function 'suspend' to be called from a coroutine
 * Make four function return a retrofit Response<T> to be checked by a generic service core extension (T could be any model)
 */
@ApiUrl(BuildConfig.API_URL)
interface RefreshService {

    @POST("auth/refresh")
    suspend fun refresh(@Body request: RefreshRequest): Response<RefreshResponse>
}