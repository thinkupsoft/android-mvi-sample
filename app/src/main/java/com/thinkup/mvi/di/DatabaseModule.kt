package com.thinkup.mvi.di

import android.content.Context
import androidx.room.Room
import com.thinkup.storage.AppDatabase
import com.thinkup.storage.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
class DatabaseModule {

    @Provides
    fun provideUserDao(
        database: AppDatabase
    ) = database.userDao()

    @Provides
    fun provideNotificationDao(
        database: AppDatabase
    ) = database.notificationDao()

    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        AppDatabase::class.java, BuildConfig.DB_NAME
    ).fallbackToDestructiveMigration().build()
}