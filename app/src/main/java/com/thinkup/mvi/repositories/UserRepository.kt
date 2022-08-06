package com.thinkup.mvi.repositories

import com.thinkup.mvi.ui.PHOTO_MIME_TYPE
import com.thinkup.common.AUTH_TOKEN_KEY
import com.thinkup.common.FCM_TOKEN_KEY
import com.thinkup.common.REFRESH_TOKEN_KEY
import com.thinkup.common.preferences.KeystoreManager
import com.thinkup.common.services.check
import com.thinkup.models.app.User
import com.thinkup.models.services.ChangePasswordRequest
import com.thinkup.models.services.EditProfileRequest
import com.thinkup.services.services.UserService
import com.thinkup.storage.dao.UserDao
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userService: UserService,
    private val userDao: UserDao,
    keystoreManager: KeystoreManager
) : BaseRepository(keystoreManager) {

    companion object {
        const val FORM_PARAM_NAME = "image"
    }

    suspend fun updateAvatar(file: File): User {
        val requestFile: RequestBody = file.asRequestBody(PHOTO_MIME_TYPE.toMediaTypeOrNull())
        val body: MultipartBody.Part = MultipartBody.Part.createFormData(FORM_PARAM_NAME, file.name, requestFile)
        val user = userService.updateAvatar(body).check()
        userDao.update(user)
        return user
    }

    suspend fun updateProfile(name: String, username: String): User? {
        userService.updateProfile(EditProfileRequest(name, username)).check()
        val user = userDao.getUser()
        user?.let {
            val newUser = it.copy(name = name, username = username)
            userDao.update(newUser)
            return newUser
        }
        return null
    }

    suspend fun deleteAccount() {
        userService.deleteAccount().check()
        keystoreManager.deleteValue(AUTH_TOKEN_KEY)
        keystoreManager.deleteValue(REFRESH_TOKEN_KEY)
        keystoreManager.deleteValue(FCM_TOKEN_KEY)
        keystoreManager.clear()
        userDao.deleteAll()
    }

    suspend fun changePassword(oldPassword: String, newPassword: String) {
        userService.changePassword(ChangePasswordRequest(oldPassword, newPassword)).check()
    }
}