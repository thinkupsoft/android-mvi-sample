package com.thinkup.mvi.di

import com.thinkup.mvi.flows.movies.ActorsTkupPagingViewModel
import com.thinkup.mvi.flows.movies.DirectorsTkupPagingViewModel
import com.thinkup.mvi.flows.movies.MoviesTkupPagingViewModel
import com.thinkup.mvi.flows.notifications.NotificationTkupPagingViewModel
import com.thinkup.mvi.ui.shared.TkupPaging
import com.thinkup.mvi.ui.shared.TkupPagingViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NotificationPagingClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MoviesPagingClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ActorsPagingClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DirectorsPagingClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NotificationPagingVM

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ActorsPagingVM

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DirectorsPagingVM

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @NotificationPagingVM
    @Provides
    fun provideNotificationPagingViewModel(
        @NotificationPagingClient viewModel: TkupPaging
    ): TkupPagingViewModel = TkupPagingViewModel(viewModel)

    @Provides
    fun provideMoviesPagingViewModel(
        @MoviesPagingClient viewModel: TkupPaging
    ): TkupPagingViewModel = TkupPagingViewModel(viewModel)

    @ActorsPagingVM
    @Provides
    fun provideActorsPagingViewModel(
        @ActorsPagingClient viewModel: TkupPaging
    ): TkupPagingViewModel = TkupPagingViewModel(viewModel)

    @DirectorsPagingVM
    @Provides
    fun provideDirectorsPagingViewModel(
        @DirectorsPagingClient viewModel: TkupPaging
    ): TkupPagingViewModel = TkupPagingViewModel(viewModel)

    @NotificationPagingClient
    @Provides
    fun provideNotificationPaging(
        viewModel: NotificationTkupPagingViewModel
    ): TkupPaging = viewModel

    @MoviesPagingClient
    @Provides
    fun provideMoviePaging(
        viewModel: MoviesTkupPagingViewModel
    ): TkupPaging = viewModel

    @ActorsPagingClient
    @Provides
    fun provideActorPaging(
        viewModel: ActorsTkupPagingViewModel
    ): TkupPaging = viewModel

    @DirectorsPagingClient
    @Provides
    fun provideDirectorPaging(
        viewModel: DirectorsTkupPagingViewModel
    ): TkupPaging = viewModel
}

