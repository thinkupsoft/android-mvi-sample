package com.thinkup.mvi.flows.newmovie

import com.thinkup.common.empty
import com.thinkup.common.ui.DateUtils
import com.thinkup.models.app.Notification
import com.thinkup.mvi.R
import com.thinkup.mvi.repositories.CategoriesRepository
import com.thinkup.mvi.repositories.MovieRepository
import com.thinkup.mvi.repositories.NotificationRepository
import com.thinkup.mvi.state.BaseViewModel
import com.thinkup.mvi.ui.shared.ANIMATION_VALUE
import com.thinkup.mvi.ui.shared.AlertMessage
import com.thinkup.mvi.ui.shared.Type
import com.thinkup.mvi.utils.NotificationHelper
import com.thinkup.mvi.utils.ResourcesHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class NewMovieViewModel @Inject constructor(
    private val categoriesRepository: CategoriesRepository,
    private val notificationRepository: NotificationRepository,
    private val movieRepository: MovieRepository,
    private val resourcesHelper: ResourcesHelper,
    private val notificationHelper: NotificationHelper
) : BaseViewModel<NewMovieState, NewMovieUiEvent>() {

    private val reducer = NewMovieReducer(NewMovieState())

    override val state: StateFlow<NewMovieState>
        get() = reducer.state

    fun onChangeTitle(title: String) {
        reducer.sendEvent(NewMovieUiEvent.OnChangeTitle(title))
    }

    fun onChangeSubtitle(subtitle: String) {
        reducer.sendEvent(NewMovieUiEvent.OnChangeSubtitle(subtitle))
    }

    fun onChangeActors(actors: String) {
        reducer.sendEvent(NewMovieUiEvent.OnChangeActors(actors))
    }

    fun onChangeDirector(director: String) {
        reducer.sendEvent(NewMovieUiEvent.OnChangeDirector(director))
    }

    fun onChangePublicationDate(publicationDate: String) {
        reducer.sendEvent(NewMovieUiEvent.OnChangePublicationDate(publicationDate))
    }

    fun onChangeDescription(description: String) {
        reducer.sendEvent(NewMovieUiEvent.OnChangeDescription(description))
    }

    fun onSelectCategory(id: Int, selected: Boolean) {
        reducer.sendEvent(NewMovieUiEvent.OnSelectCategory(id, selected))
    }

    fun onSelectImage(imageUpload: ImageUpload) {
        reducer.sendEvent(NewMovieUiEvent.OnSelectImage(imageUpload))
    }

    fun onBack() {
        reducer.sendEvent(NewMovieUiEvent.OnBack)
    }

    fun onHideAlert() {
        executeService(service = {
            val success = reducer.state.value.alertMessage?.isSuccess ?: false
            reducer.sendEvent(NewMovieUiEvent.HideAlertMessage)
            delay(ANIMATION_VALUE.toLong())
            if (success) reducer.sendEvent(NewMovieUiEvent.GoBack)
        })
    }

    fun getDateFormat() = resourcesHelper.getDateFormat()
    fun getDateMask() = resourcesHelper.getDateMask()

    fun getCategories() {
        executeService(
            service = {
                reducer.sendEvent(NewMovieUiEvent.OnClick(Type.SOFT))
                reducer.sendEvent(NewMovieUiEvent.GetCategories(categoriesRepository.getCategories()))
            }
        )
    }

    private fun validateDataStepOne(): Boolean {
        val hasTitleError = state.value.title.isNullOrEmpty()
        val hasActorsError = state.value.actors.isNullOrEmpty()
        val hasDateError = if (state.value.publicationDate.isNullOrEmpty()) false else {
            val date = resourcesHelper.isValidDate(state.value.publicationDate.orEmpty())
            date == null || !date.before(Date())
        }
        val hasError = hasTitleError || hasActorsError || hasDateError
        if (hasError) {
            reducer.sendEvent(
                NewMovieUiEvent.OnDataError(
                    alertMessage = AlertMessage(
                        message = resourcesHelper.getString(R.string.app_error),
                        isSuccess = false
                    ),
                    isTitleError = if (hasTitleError) resourcesHelper.getString(R.string.required_field) else String.empty(),
                    isActorsError = if (hasActorsError) resourcesHelper.getString(R.string.required_field) else String.empty(),
                    isDateError = if (hasDateError) resourcesHelper.getString(R.string.invalid_date) else String.empty()
                )
            )
        }
        return !hasError
    }

    private fun validateDataStepTwo(): Boolean {
        val hasImagesError = state.value.images.isEmpty()
        val hasCategoriesError = state.value.categories.none { it.selected }
        val hasError = hasImagesError || hasCategoriesError
        if (hasError) {
            reducer.sendEvent(
                NewMovieUiEvent.OnDataError(
                    alertMessage = AlertMessage(
                        message = resourcesHelper.getString(R.string.app_error),
                        isSuccess = false
                    ),
                    isImagesError = if (hasImagesError) resourcesHelper.getString(R.string.required_field_one) else String.empty(),
                    isCategoriesError = if (hasCategoriesError) resourcesHelper.getString(R.string.required_field_one) else String.empty()
                )
            )
        }
        return !hasError
    }

    fun next() {
        executeService(
            service = {
                reducer.sendEvent(NewMovieUiEvent.OnClick(Type.SOFT))
                if (validateDataStepOne()) {
                    reducer.sendEvent(NewMovieUiEvent.OnNext)
                }
            }
        )
    }

    fun save() {
        executeService(
            service = {
                reducer.sendEvent(NewMovieUiEvent.OnClick(Type.FULL))
                if (validateDataStepTwo()) {
                    val publicationDate = resourcesHelper.isValidDate(state.value.publicationDate.orEmpty())
                    val formattedDate = if (publicationDate == null) null
                    else DateUtils().format(publicationDate, DateUtils.FORMAT_DATE1)
                    movieRepository.addMovie(
                        title = state.value.title.orEmpty(),
                        subtitle = state.value.subtitle,
                        actors = state.value.actors.orEmpty(),
                        director = state.value.director,
                        publicationDate = formattedDate,
                        description = state.value.description,
                        categories = state.value.categories.filter { it.selected },
                        images = state.value.images.map {
                            it.bitmap?.let {
                                resourcesHelper.saveBitmap(resourcesHelper.getFileTempDir().path, it)
                            } ?: run {
                                it.uri?.let {
                                    resourcesHelper.getFile(it)
                                }
                            }
                        }.requireNoNulls()
                    )
                    reducer.sendEvent(
                        NewMovieUiEvent.OnSuccess(
                            alertMessage = AlertMessage(
                                message = resourcesHelper.getString(R.string.success),
                                isSuccess = true
                            )
                        )
                    )
                    notificationHelper.showNotification(
                        resourcesHelper.getString(R.string.new_movie_header),
                        state.value.title.orEmpty(),
                        mapOf()
                    )
                    notificationRepository.addNotification(
                        Notification(
                            title = resourcesHelper.getString(R.string.new_movie_header),
                            body = state.value.title.orEmpty()
                        )
                    )
                }
            },
            onErrorCallback = {
                reducer.sendEvent(
                    NewMovieUiEvent.OnDataError(
                        alertMessage = AlertMessage(
                            message = resourcesHelper.getString(R.string.app_error),
                            isSuccess = false
                        )
                    )
                )
            }
        )
    }
}