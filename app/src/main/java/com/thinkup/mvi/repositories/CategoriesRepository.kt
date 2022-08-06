package com.thinkup.mvi.repositories

import com.thinkup.common.preferences.KeystoreManager
import com.thinkup.common.services.check
import com.thinkup.services.services.CategoriesService
import javax.inject.Inject

class CategoriesRepository @Inject constructor(
    private val service: CategoriesService,
    keystoreManager: KeystoreManager
) : BaseRepository(keystoreManager) {

    suspend fun getCategories() = service.getCategories().check().categories
}