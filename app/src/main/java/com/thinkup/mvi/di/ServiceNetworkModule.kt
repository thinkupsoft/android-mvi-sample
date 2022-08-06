package com.thinkup.mvi.di

import android.content.Context
import com.thinkup.services.core.CacheFileUtil
import com.thinkup.services.core.CustomCache
import com.thinkup.services.core.ServiceFactory
import com.thinkup.services.services.AuthService
import com.thinkup.services.services.MovieService
import com.thinkup.services.services.CategoriesService
import com.thinkup.services.services.NotificationService
import com.thinkup.services.services.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File

const val CACHE_DIR = "http-cache"

@Module
@InstallIn(ViewModelComponent::class)
object ServiceNetworkModule {

    @Provides
    fun provideAuthService(
        serviceFactory: ServiceFactory
    ): AuthService = serviceFactory.createInstance(AuthService::class.java)

    @Provides
    fun provideUserService(
        serviceFactory: ServiceFactory
    ): UserService = serviceFactory.createInstance(UserService::class.java)

    @Provides
    fun provideNotificationService(
        serviceFactory: ServiceFactory
    ): NotificationService = serviceFactory.createInstance(NotificationService::class.java)

    @Provides
    fun provideCategoriesService(
        serviceFactory: ServiceFactory
    ): CategoriesService = serviceFactory.createInstance(CategoriesService::class.java)

    @Provides
    fun provideMovieService(
        serviceFactory: ServiceFactory
    ): MovieService = serviceFactory.createInstance(MovieService::class.java)

    @Provides
    fun provideServiceFactory(
        @ApplicationContext context: Context,
        postCache: CustomCache
    ): ServiceFactory = ServiceFactory(context, postCache)

    @Provides
    fun provideCustomCache(
        cacheFileUtil: CacheFileUtil,
        cacheDir: File
    ): CustomCache = CustomCache(cacheDir, cacheFileUtil)

    @Provides
    fun provideCacheFileUtil(): CacheFileUtil = CacheFileUtil()

    @Provides
    fun provideCacheDir(
        @ApplicationContext context: Context
    ): File = File(context.cacheDir, CACHE_DIR)
}