package com.thinkup.mvi.flows.newmovie

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.thinkup.mvi.R
import com.thinkup.mvi.compose.TkupAppState
import com.thinkup.mvi.compose.rememberTkupAppState
import com.thinkup.mvi.ui.CAMERA_IMAGE_SOURCE
import com.thinkup.mvi.ui.GALLERY_IMAGE_SOURCE
import com.thinkup.mvi.ui.MAX_IMAGES_MOVIE
import com.thinkup.mvi.ui.SCREEN_RESULT
import com.thinkup.mvi.ui.shared.TkupButton
import com.thinkup.mvi.ui.shared.TkupButtonConfig
import com.thinkup.mvi.ui.shared.TkupCategoryTagConfig
import com.thinkup.mvi.ui.shared.TkupCustomLoader
import com.thinkup.mvi.ui.shared.TkupCustomLoaderConfig
import com.thinkup.mvi.ui.shared.TkupCustomSuccess
import com.thinkup.mvi.ui.shared.TkupCustomSuccessConfig
import com.thinkup.mvi.ui.shared.TkupDialog
import com.thinkup.mvi.ui.shared.TkupImageSource
import com.thinkup.mvi.ui.shared.TkupImageUploaderConfig
import com.thinkup.mvi.ui.shared.TkupInput
import com.thinkup.mvi.ui.shared.TkupInputConfig
import com.thinkup.mvi.ui.shared.TkupNavigationTitle
import com.thinkup.mvi.ui.shared.TkupCategoriesContainer
import com.thinkup.mvi.ui.shared.TkupImageUploader
import com.thinkup.mvi.ui.shared.MaskVisualTransformation
import com.thinkup.mvi.ui.shared.ShowAlertMessage
import com.thinkup.mvi.ui.theme.Background
import com.thinkup.mvi.ui.theme.ThinkUpTheme
import com.thinkup.mvi.ui.theme.DIMEN_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_XX_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_TRIPLE_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_X_SMALL
import com.thinkup.mvi.ui.theme.Error
import com.thinkup.mvi.ui.theme.Gray2
import com.thinkup.mvi.ui.theme.Typography
import com.thinkup.mvi.ui.theme.inputErrorText
import com.thinkup.mvi.utils.launchCameraPicker
import com.thinkup.mvi.utils.launchImagePicker
import kotlinx.coroutines.launch

@Composable
fun NewMovieScreen(
    viewModel: NewMovieViewModel = hiltViewModel(),
    navController: NavController
) {

    val state by viewModel.state.collectAsState()

    if (state.goBack) {
        navController.previousBackStackEntry
            ?.savedStateHandle
            ?.set(SCREEN_RESULT, true)
        navController.popBackStack()
    }

    if (state.step == 0) NewMovieStepOne(viewModel, navController, state)
    else NewMovieStepTwo(viewModel, state)

    if (state.alertMessage?.isSuccess == true) {
        TkupCustomSuccess(
            config = TkupCustomSuccessConfig(
                visible = true,
                title = stringResource(id = R.string.well_done),
                message = stringResource(id = R.string.new_movie_success_message)
            ),
            onHideAlertMessage = { viewModel.onHideAlert() }
        )
    } else {
        ShowAlertMessage(
            alertMessage = state.alertMessage,
            onHideAlertMessage = {
                viewModel.onHideAlert()
            },
            backgroundContent = {}
        )
    }

    TkupCustomLoader(
        TkupCustomLoaderConfig(
            visible = state.showLoading,
            type = state.loadingType,
            text = stringResource(id = R.string.saving)
        )
    )
}

@Composable
private fun NewMovieStepOne(
    viewModel: NewMovieViewModel = hiltViewModel(),
    navController: NavController,
    state: NewMovieState
) {
    Column(
        modifier = Modifier.background(Background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .padding(start = DIMEN_XX_NORMAL, end = DIMEN_XX_NORMAL)
                .weight(1f)
        ) {
            item {
                TkupNavigationTitle(
                    title = if (state.title.isNullOrEmpty()) stringResource(id = R.string.new_movie_header) else state.title.uppercase()
                ) {
                    navController.navigateUp()
                }
            }

            item {

                // Inputs
                TkupInput(
                    modifier = Modifier.padding(top = DIMEN_XX_NORMAL),
                    TkupInputConfig(
                        text = state.title.orEmpty(),
                        placeholder = stringResource(id = R.string.new_movie_title),
                        keyboardType = KeyboardType.Text,
                        errorMessage = state.isTitleError
                    )
                ) { viewModel.onChangeTitle(it) }

                TkupInput(
                    modifier = Modifier.padding(top = DIMEN_X_SMALL),
                    TkupInputConfig(
                        text = state.subtitle.orEmpty(),
                        placeholder = stringResource(id = R.string.new_movie_subtitle),
                        keyboardType = KeyboardType.Text
                    )
                ) { viewModel.onChangeSubtitle(it) }

                TkupInput(
                    modifier = Modifier.padding(top = DIMEN_X_SMALL),
                    TkupInputConfig(
                        text = state.actors.orEmpty(),
                        placeholder = stringResource(id = R.string.new_movie_actors),
                        keyboardType = KeyboardType.Text,
                        errorMessage = state.isActorsError
                    )
                ) { viewModel.onChangeActors(it) }

                TkupInput(
                    modifier = Modifier.padding(top = DIMEN_X_SMALL),
                    TkupInputConfig(
                        text = state.director.orEmpty(),
                        placeholder = stringResource(id = R.string.new_movie_director),
                        keyboardType = KeyboardType.Text
                    )
                ) { viewModel.onChangeDirector(it) }

                TkupInput(
                    modifier = Modifier.padding(top = DIMEN_X_SMALL),
                    TkupInputConfig(
                        text = state.publicationDate.orEmpty(),
                        customVisualTransformation = MaskVisualTransformation(viewModel.getDateMask()),
                        maxLength = 8,
                        placeholder = "${stringResource(id = R.string.new_movie_date)} (${viewModel.getDateFormat()})",
                        keyboardType = KeyboardType.Number,
                        errorMessage = state.isDateError
                    )
                ) { viewModel.onChangePublicationDate(it) }

                TkupInput(
                    modifier = Modifier.padding(top = DIMEN_X_SMALL, bottom = DIMEN_X_SMALL),
                    TkupInputConfig(
                        text = state.description.orEmpty(),
                        placeholder = stringResource(id = R.string.new_movie_description),
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                        isSingleLine = false
                    )
                ) { viewModel.onChangeDescription(it) }
            }
        }
        // New movie button
        Divider(Modifier.background(Gray2))
        TkupButton(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(start = DIMEN_XX_NORMAL, end = DIMEN_XX_NORMAL, bottom = DIMEN_NORMAL, top = DIMEN_NORMAL),
            config = TkupButtonConfig(
                text = stringResource(id = R.string.next),
            )
        ) { viewModel.next() }
    }
}

@Composable
private fun NewMovieStepTwo(
    viewModel: NewMovieViewModel = hiltViewModel(),
    state: NewMovieState
) {

    val openSourceDialog = remember { mutableStateOf(false) }
    val openCameraPreview = remember { mutableStateOf(false) }
    val openGalleryPreview = remember { mutableStateOf(false) }
    onOpenSourceDialog(openSourceDialog, openCameraPreview, openGalleryPreview)
    openCameraPreview(viewModel, openSourceDialog, openCameraPreview)
    openPickerImageDialog(viewModel, openSourceDialog, openGalleryPreview)

    if (state.categories.isEmpty() && !state.showLoading) viewModel.getCategories()

    Column(
        modifier = Modifier.background(Background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .padding(start = DIMEN_XX_NORMAL, end = DIMEN_XX_NORMAL)
                .weight(1f)
        ) {
            item {
                TkupNavigationTitle(
                    title = state.title.orEmpty().uppercase()
                ) {
                    viewModel.onBack()
                }
            }

            item {
                Text(
                    text = stringResource(id = R.string.new_movie_images),
                    modifier = Modifier.padding(top = DIMEN_TRIPLE_NORMAL),
                    style = Typography.titleMedium
                )
                if (state.isImagesError.isNotEmpty())
                    Row(
                        modifier = Modifier
                            .height(DIMEN_XX_NORMAL)
                            .background(Error)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.padding(DIMEN_X_SMALL))
                        Text(text = state.isImagesError, style = Typography.inputErrorText)
                        Spacer(modifier = Modifier.padding(DIMEN_X_SMALL))
                    }
                Row(
                    horizontalArrangement = if (state.images.size < MAX_IMAGES_MOVIE) Arrangement.spacedBy(DIMEN_NORMAL) else Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = DIMEN_XX_NORMAL)
                ) {
                    state.images.forEach {
                        TkupImageUploader(
                            TkupImageUploaderConfig(
                                bitmap = it.bitmap,
                                uri = it.uri
                            ) { openSourceDialog.value = true }
                        )
                    }
                    // add empty uploader
                    if (state.images.size < MAX_IMAGES_MOVIE) TkupImageUploader(TkupImageUploaderConfig { openSourceDialog.value = true })
                }
            }

            item {
                Text(
                    text = stringResource(id = R.string.new_movie_categories),
                    modifier = Modifier.padding(top = DIMEN_TRIPLE_NORMAL),
                    style = Typography.titleMedium
                )
                if (state.isCategoriesError.isNotEmpty())
                    Row(
                        modifier = Modifier
                            .height(DIMEN_XX_NORMAL)
                            .background(Error)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.padding(DIMEN_X_SMALL))
                        Text(text = state.isCategoriesError, style = Typography.inputErrorText)
                        Spacer(modifier = Modifier.padding(DIMEN_X_SMALL))
                    }
                TkupCategoriesContainer(Modifier.padding(bottom = DIMEN_XX_NORMAL), state.categories.map {
                    TkupCategoryTagConfig(
                        id = it.id,
                        label = it.name,
                        selected = it.selected
                    ) { selected, id -> viewModel.onSelectCategory(id, selected) }
                })
            }
        }
        // New movie button
        Divider(Modifier.background(Gray2))
        TkupButton(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(start = DIMEN_XX_NORMAL, end = DIMEN_XX_NORMAL, bottom = DIMEN_NORMAL, top = DIMEN_NORMAL),
            config = TkupButtonConfig(
                text = stringResource(id = R.string.save),
            )
        ) { viewModel.save() }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NewMovieScreenPreview() {
    ThinkUpTheme(darkTheme = false) {
        NewMovieScreen(navController = rememberNavController())
    }
}

@Composable
fun onOpenSourceDialog(
    openSourceDialog: MutableState<Boolean>,
    openCameraPreview: MutableState<Boolean>,
    openGalleryPreview: MutableState<Boolean>
) {
    // open dialog image source
    if (openSourceDialog.value) TkupDialog(onDismiss = { openSourceDialog.value = false }) {
        TkupImageSource {
            when (it) {
                GALLERY_IMAGE_SOURCE -> {
                    openGalleryPreview.value = true
                }
                CAMERA_IMAGE_SOURCE -> {
                    openCameraPreview.value = true
                }
            }
        }
    }
}

@Composable
fun openPickerImageDialog(
    viewModel: NewMovieViewModel,
    openSourceDialog: MutableState<Boolean>,
    openGalleryPreview: MutableState<Boolean>,
    appState: TkupAppState = rememberTkupAppState(),
) {
    if (openGalleryPreview.value) {
        launchImagePicker {
            openGalleryPreview.value = false
            openSourceDialog.value = false
            it?.let {
                appState.coroutineScope.launch {
                    viewModel.onSelectImage(ImageUpload(uri = it))
                }
            }
        }
    }
}

@Composable
fun openCameraPreview(
    viewModel: NewMovieViewModel,
    openSourceDialog: MutableState<Boolean>,
    openCameraPreview: MutableState<Boolean>,
    appState: TkupAppState = rememberTkupAppState(),
) {
    if (openCameraPreview.value) {
        launchCameraPicker {
            openCameraPreview.value = false
            openSourceDialog.value = false
            it?.let {
                appState.coroutineScope.launch {
                    viewModel.onSelectImage(ImageUpload(bitmap = it))
                }
            }
        }
    }
}