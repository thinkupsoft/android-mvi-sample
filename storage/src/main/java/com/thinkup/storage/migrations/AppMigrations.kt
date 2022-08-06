package com.thinkup.storage.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object AppMigrations {

    /**
     * @sample:
     * database.execSQL(
            "CREATE TABLE notification (id  INTEGER NOT NULL, title TEXT NOT NULL," +
            " description TEXT NOT NULL," +
            " isRead INTEGER NOT NULL," +
            " created NUMBER, PRIMARY KEY(id))"
        )
     */
    val MIGRATION_0_1: Migration = object : Migration(0, 1) {
        override fun migrate(database: SupportSQLiteDatabase) {
        }
    }
}