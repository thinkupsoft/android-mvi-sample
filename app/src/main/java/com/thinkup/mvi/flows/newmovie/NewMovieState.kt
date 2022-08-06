package com.thinkup.mvi.flows.newmovie

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.Immutable
import com.thinkup.mvi.state.Reducer
import com.thinkup.mvi.state.UiEvent
import com.thinkup.mvi.state.UiState
import com.thinkup.mvi.ui.shared.AlertMessage
import com.thinkup.mvi.ui.shared.Type
import com.thinkup.common.empty
import com.thinkup.models.app.Category

data class ImageUpload(
    val id: String = String.empty(),
    val uri: Uri? = null,
    val bitmap: Bitmap? = null
)

@Immutable
sealed class NewMovieUiEvent : UiEvent {
    object OnNext : NewMovieUiEvent()
    object OnBack : NewMovieUiEvent()
    data class OnClick(val loadingType: Type) : NewMovieUiEvent()
    data class OnChangeTitle(val title: String) : NewMovieUiEvent()
    data class OnChangeSubtitle(val subtitle: String) : NewMovieUiEvent()
    data class OnChangeActors(val actors: String) : NewMovieUiEvent()
    data class OnChangeDirector(val director: String) : NewMovieUiEvent()
    data class OnChangePublicationDate(val publicationDate: String) : NewMovieUiEvent()
    data class OnChangeDescription(val description: String) : NewMovieUiEvent()
    data class OnSelectCategory(val id: Int, val selected: Boolean) : NewMovieUiEvent()
    data class OnSelectImage(val imageUpload: ImageUpload) : NewMovieUiEvent()
    data class OnDataError(
        val alertMessage: AlertMessage? = null,
        val isTitleError: String = String.empty(),
        val isActorsError: String = String.empty(),
        val isDateError: String = String.empty(),
        val isImagesError: String = String.empty(),
        val isCategoriesError: String = String.empty()
    ) : NewMovieUiEvent()

    object HideAlertMessage : NewMovieUiEvent()
    data class GetCategories(val categories: List<Category>) : NewMovieUiEvent()
    object GoBack : NewMovieUiEvent()
    data class OnSuccess(val alertMessage: AlertMessage? = null) : NewMovieUiEvent()
}

@Immutable
data class NewMovieState(
    val showLoading: Boolean = false,
    val loadingType: Type = Type.FULL,
    val title: String? = null,
    val subtitle: String? = null,
    val actors: String? = null,
    val director: String? = null,
    val publicationDate: String? = null,
    val description: String? = null,
    val alertMessage: AlertMessage? = null,
    val isTitleError: String = String.empty(),
    val isActorsError: String = String.empty(),
    val isDateError: String = String.empty(),
    val isImagesError: String = String.empty(),
    val isCategoriesError: String = String.empty(),
    val step: Int = 0,
    val goBack: Boolean = false,
    val categories: List<Category> = emptyList(),
    val images: MutableList<ImageUpload> = mutableListOf(),
    val categoriesCount: Int = 0,
    val imagesCount: Int = 0,
) : UiState

class NewMovieReducer(initial: NewMovieState) : Reducer<NewMovieState, NewMovieUiEvent>(initial) {
    override fun reduce(oldState: NewMovieState, event: NewMovieUiEvent) {
        when (event) {
            is NewMovieUiEvent.OnNext -> {
                setState(oldState.copy(step = 1, showLoading = false))
            }
            is NewMovieUiEvent.OnBack -> {
                setState(oldState.copy(step = 0, showLoading = false))
            }
            is NewMovieUiEvent.OnClick -> {
                setState(
                    oldState.copy(
                        showLoading = true,
                        loadingType = event.loadingType,
                        isTitleError = String.empty(),
                        isActorsError = String.empty(),
                        isDateError = String.empty(),
                        isCategoriesError = String.empty(),
                        isImagesError = String.empty()
                    )
                )
            }
            NewMovieUiEvent.GoBack -> {
                setState(oldState.copy(goBack = true))
            }
            NewMovieUiEvent.HideAlertMessage -> {
                setState(oldState.copy(showLoading = false, alertMessage = null))
            }
            is NewMovieUiEvent.OnChangeTitle -> {
                setState(oldState.copy(title = event.title))
            }
            is NewMovieUiEvent.OnChangeSubtitle -> {
                setState(oldState.copy(subtitle = event.subtitle))
            }
            is NewMovieUiEvent.OnChangeActors -> {
                setState(oldState.copy(actors = event.actors))
            }
            is NewMovieUiEvent.OnChangeDirector -> {
                setState(oldState.copy(director = event.director))
            }
            is NewMovieUiEvent.OnChangePublicationDate -> {
                setState(oldState.copy(publicationDate = event.publicationDate))
            }
            is NewMovieUiEvent.OnChangeDescription -> {
                setState(oldState.copy(description = event.description))
            }
            is NewMovieUiEvent.OnDataError -> {
                setState(
                    oldState.copy(
                        showLoading = false,
                        alertMessage = event.alertMessage,
                        isTitleError = event.isTitleError,
                        isActorsError = event.isActorsError,
                        isDateError = event.isDateError,
                        isCategoriesError = event.isCategoriesError,
                        isImagesError = event.isImagesError
                    )
                )
            }
            is NewMovieUiEvent.OnSuccess -> {
                setState(
                    oldState.copy(
                        alertMessage = event.alertMessage,
                        showLoading = false,
                        isTitleError = String.empty(),
                        isActorsError = String.empty(),
                        isDateError = String.empty(),
                        isCategoriesError = String.empty(),
                        isImagesError = String.empty()
                    )
                )
            }
            is NewMovieUiEvent.GetCategories -> {
                setState(oldState.copy(categories = event.categories, showLoading = false))
            }
            is NewMovieUiEvent.OnSelectCategory -> {
                oldState.categories.find { it.id == event.id }?.selected = event.selected
                setState(oldState.copy(categoriesCount = oldState.categories.count { it.selected }))
            }
            is NewMovieUiEvent.OnSelectImage -> {
                oldState.images.add(event.imageUpload)
                setState(oldState.copy(imagesCount = oldState.images.size))
            }
        }
    }
}