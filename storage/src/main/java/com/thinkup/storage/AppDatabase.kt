package com.thinkup.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.thinkup.common.converters.CommonConverters
import com.thinkup.models.app.Notification
import com.thinkup.models.app.User
import com.thinkup.storage.dao.NotificationDao
import com.thinkup.storage.dao.UserDao

@Database(
    // Declare yours entities/tables classes
    entities = [
        User::class,
        Notification::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(CommonConverters::class)
abstract class AppDatabase : RoomDatabase() {
    /**
     * Put here all yours DAO instances
     */
    abstract fun userDao(): UserDao
    abstract fun notificationDao(): NotificationDao
}