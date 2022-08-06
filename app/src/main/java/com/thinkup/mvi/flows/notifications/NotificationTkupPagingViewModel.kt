package com.thinkup.mvi.flows.notifications

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.thinkup.mvi.ui.shared.Options
import com.thinkup.mvi.ui.shared.TkupCustomPaging
import com.thinkup.mvi.ui.shared.TkupPaging
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotificationTkupPagingViewModel @Inject constructor() : TkupPaging() {

    // TODO
    override fun getPage(options: Options): Flow<PagingData<Any>> = Pager(PagingConfig(pageSize = options.getPageLimit())) {
        object : TkupCustomPaging() {
            override suspend fun getPage(nextPage: Int): List<Any> {
                return try {
                    listOf<Any>()
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    throw ex
                }
            }
        }
    }.flow
}