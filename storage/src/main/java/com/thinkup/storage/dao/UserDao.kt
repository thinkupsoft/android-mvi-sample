package com.thinkup.storage.dao

import androidx.room.Dao
import androidx.room.Query
import com.thinkup.models.app.User

@Dao
interface UserDao : IDao<User> {

    @Query("SELECT * FROM user LIMIT 1")
    suspend fun getUser(): User?

    @Query("DELETE FROM user")
    suspend fun deleteAll()
}