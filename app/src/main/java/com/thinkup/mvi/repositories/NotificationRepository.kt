package com.thinkup.mvi.repositories

import com.thinkup.common.FCM_TOKEN_KEY
import com.thinkup.common.preferences.KeystoreManager
import com.thinkup.common.services.check
import com.thinkup.models.app.Notification
import com.thinkup.models.services.DeviceRequest
import com.thinkup.services.services.NotificationService
import com.thinkup.storage.dao.NotificationDao
import javax.inject.Inject

class NotificationRepository @Inject constructor(
    private val service: NotificationService,
    private val notificationDao: NotificationDao,
    keystoreManager: KeystoreManager
) : BaseRepository(keystoreManager) {

    suspend fun addDeviceIfNeeded(fcmToken: String) {
        if (isUserLogged()) addDevice(fcmToken)
    }

    suspend fun addDevice(fcmToken: String) {
        keystoreManager.setValue(FCM_TOKEN_KEY, fcmToken)
        service.addDevice(DeviceRequest(fcmToken)).check()
    }

    suspend fun removeDevice() {
        val device = keystoreManager.getValue(FCM_TOKEN_KEY).orEmpty()
        service.deleteDevice(DeviceRequest(device)).check()
    }

    suspend fun addNotification(notification: Notification) = notificationDao.insert(notification)

    suspend fun getNotifications() = notificationDao.getNotifications()

    suspend fun deleteNotification(notification: Notification) = notificationDao.delete(notification)

}