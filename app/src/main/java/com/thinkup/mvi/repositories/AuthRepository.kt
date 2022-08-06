package com.thinkup.mvi.repositories

import com.thinkup.common.AUTH_TOKEN_KEY
import com.thinkup.common.FCM_TOKEN_KEY
import com.thinkup.common.REFRESH_TOKEN_KEY
import com.thinkup.common.preferences.KeystoreManager
import com.thinkup.common.services.check
import com.thinkup.models.services.ForgotPasswordRequest
import com.thinkup.models.services.LoginRequest
import com.thinkup.models.services.LogoutRequest
import com.thinkup.models.services.RedeemPasswordRequest
import com.thinkup.models.services.RegisterRequest
import com.thinkup.models.services.UserDataResponse
import com.thinkup.services.services.AuthService
import com.thinkup.storage.dao.UserDao
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authService: AuthService,
    private val userDao: UserDao,
    keystoreManager: KeystoreManager
) : BaseRepository(keystoreManager) {

    private suspend fun setToken(accessToken: String) = keystoreManager.setValue(AUTH_TOKEN_KEY, accessToken)

    private suspend fun setRefresh(refreshToken: String) = keystoreManager.setValue(REFRESH_TOKEN_KEY, refreshToken)

    private suspend fun setUserData(result: UserDataResponse) {
        setToken(result.accessToken)
        setRefresh(result.refreshToken)
        userDao.insert(result.user)
    }

    suspend fun login(email: String, password: String): UserDataResponse {
        val result = authService.login(LoginRequest(email, password)).check()
        setUserData(result)

        return result
    }

    suspend fun register(name: String, email: String, password: String): UserDataResponse {
        val result = authService.register(RegisterRequest(name, email, password)).check()
        setUserData(result)

        return result
    }

    suspend fun getCurrentUser() = userDao.getUser()

    suspend fun logout() {
        authService.logout(
            LogoutRequest(
                keystoreManager.getValue(REFRESH_TOKEN_KEY).orEmpty(),
                keystoreManager.getValue(FCM_TOKEN_KEY).orEmpty()
            )
        )
        keystoreManager.deleteValue(AUTH_TOKEN_KEY)
        keystoreManager.deleteValue(REFRESH_TOKEN_KEY)
        keystoreManager.deleteValue(FCM_TOKEN_KEY)
        keystoreManager.clear()
        userDao.deleteAll()
    }

    suspend fun forgotPassword(email: String) {
        authService.forgotPassword(ForgotPasswordRequest(email)).check()
    }

    suspend fun redeemPassword(token: String, newPassword: String) {
        authService.redeemPassword(RedeemPasswordRequest(token, newPassword)).check()
    }
}