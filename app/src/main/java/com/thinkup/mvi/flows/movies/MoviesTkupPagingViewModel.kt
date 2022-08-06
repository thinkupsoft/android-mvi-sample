package com.thinkup.mvi.flows.movies

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.thinkup.mvi.repositories.MovieRepository
import com.thinkup.mvi.ui.shared.Options
import com.thinkup.mvi.ui.shared.TkupCustomPaging
import com.thinkup.mvi.ui.shared.TkupPaging
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MoviesTkupPagingViewModel @Inject constructor(
    private val repository: MovieRepository
) : TkupPaging() {

    object Header

    override fun getPage(options: Options): Flow<PagingData<Any>> = Pager(PagingConfig(pageSize = options.getPageLimit())) {
        object : TkupCustomPaging() {
            override suspend fun getPage(nextPage: Int): List<Any> {
                return try {
                    val data: MutableList<Any> = mutableListOf<Any>().apply {
                        addAll(repository.getMyMovies((nextPage - 1) * options.getPageLimit(), options.getPageLimit()).data)
                    }
                    if (nextPage == 1 && data.isNotEmpty()) data.apply { add(0, Header) }
                    else data
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    throw ex
                }
            }
        }
    }.flow
}