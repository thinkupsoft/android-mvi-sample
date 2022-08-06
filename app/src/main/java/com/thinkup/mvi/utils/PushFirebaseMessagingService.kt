package com.thinkup.mvi.utils

import android.util.Log
import androidx.room.Room
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.thinkup.common.preferences.KeystoreManager
import com.thinkup.mvi.di.CACHE_DIR
import com.thinkup.mvi.repositories.NotificationRepository
import com.thinkup.services.core.CacheFileUtil
import com.thinkup.services.core.CustomCache
import com.thinkup.services.core.ServiceFactory
import com.thinkup.services.services.NotificationService
import com.thinkup.storage.AppDatabase
import com.thinkup.storage.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class PushFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        const val CHANNEL_HIGH_IMPORTANCE = "tkup_notification_channel"
        const val CHANNEL_NAME = "ThinkUp - notification_channel"
    }

    private val notificationHelper by lazy { NotificationHelper(this) }

    private val notificationRepository: NotificationRepository by lazy {
        val serviceFactory = ServiceFactory(this, CustomCache(File(applicationContext.cacheDir, CACHE_DIR), CacheFileUtil()))
        NotificationRepository(
            service = serviceFactory.createInstance(NotificationService::class.java),
            notificationDao = Room.databaseBuilder(
                this,
                AppDatabase::class.java, BuildConfig.DB_NAME
            ).fallbackToDestructiveMigration().build().notificationDao(),
            keystoreManager = KeystoreManager(this)
        )
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        notificationHelper.showNotification(remoteMessage.notification?.title, remoteMessage.notification?.body, remoteMessage.data)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM_TOKEN::", token)
        addDevice(token)
    }

    private fun addDevice(token: String) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                notificationRepository.addDeviceIfNeeded(token)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
}