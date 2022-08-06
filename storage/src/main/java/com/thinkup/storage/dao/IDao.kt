package com.thinkup.storage.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface IDao<E> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: E)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg entity: E)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(entity: E)

    @Delete
    suspend fun delete(entity: E)
}