package com.thinkup.mvi.repositories

import com.thinkup.mvi.ui.PHOTO_MIME_TYPE
import com.thinkup.common.preferences.KeystoreManager
import com.thinkup.common.services.check
import com.thinkup.common.toRequestBody
import com.thinkup.common.tryRequestBody
import com.thinkup.models.app.Movie
import com.thinkup.models.app.Category
import com.thinkup.services.services.MovieService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val service: MovieService,
    keystoreManager: KeystoreManager
) : BaseRepository(keystoreManager) {

    companion object {
        const val FORM_PARAM_NAME = "movie"
    }

    suspend fun getFeaturedMovies() = service.getFeaturedMovies().check()

    suspend fun getActors(offset: Int, limit: Int) = service.getActors(offset, limit).check()

    suspend fun getDirectors(offset: Int, limit: Int) = service.getDirectors(offset, limit).check()

    suspend fun getMyMovies(offset: Int, limit: Int) = service.getMyMovies(offset, limit).check()

    suspend fun addMovie(
        title: String,
        subtitle: String?,
        actors: String,
        director: String?,
        publicationDate: String?,
        description: String?,
        categories: List<Category>,
        images: List<File>
    ): Movie {
        val requestFiles = mutableListOf<MultipartBody.Part>()
        images.forEachIndexed { index, file ->
            val requestFile: RequestBody = file.asRequestBody(PHOTO_MIME_TYPE.toMediaTypeOrNull())
            val body: MultipartBody.Part = MultipartBody.Part.createFormData("$FORM_PARAM_NAME${index + 1}", file.name, requestFile)
            requestFiles.add(body)
        }

        val movie = service.addMovie(
            title = title.toRequestBody(),
            subtitle = subtitle.tryRequestBody(),
            actors = actors.toRequestBody(),
            director = director.tryRequestBody(),
            publicationDate = publicationDate.tryRequestBody(),
            description = description.tryRequestBody(),
            categories = categories.map { it.id }.joinToString(",").toRequestBody(),
            files = requestFiles
        ).check()
        return movie
    }
}