package com.thinkup.common.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "tkup_settings")

class KeystoreManager @Inject constructor(@ApplicationContext val context: Context) {

    suspend fun setValue(key: String, value: String) {
        val datastoreKey = stringPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[datastoreKey] = value
        }
    }

    fun getValue(key: String, default: String? = null): String? {
        val datastoreKey = stringPreferencesKey(key)
        val preferences = runBlocking { context.dataStore.data.first() }
        return preferences[datastoreKey] ?: default
    }

    suspend fun deleteValue(key: String) {
        val datastoreKey = stringPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences.remove(datastoreKey)
        }
    }

    suspend fun clear() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}