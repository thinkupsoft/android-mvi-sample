package com.thinkup.storage.dao

import androidx.room.Dao
import androidx.room.Query
import com.thinkup.models.app.Notification
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao : IDao<Notification> {

    @Query("SELECT * FROM notification ORDER BY id DESC")
    suspend fun getNotifications(): List<Notification>?
}