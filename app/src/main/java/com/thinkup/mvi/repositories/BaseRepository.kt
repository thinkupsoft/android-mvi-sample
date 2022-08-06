package com.thinkup.mvi.repositories

import com.thinkup.common.AUTH_TOKEN_KEY
import com.thinkup.common.preferences.KeystoreManager

open class BaseRepository(protected val keystoreManager: KeystoreManager) {

    fun isUserLogged() = !keystoreManager.getValue(AUTH_TOKEN_KEY).isNullOrEmpty()
}