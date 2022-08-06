package com.thinkup.mvi.flows.movies

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.thinkup.models.app.Director
import com.thinkup.mvi.R
import com.thinkup.mvi.ui.shared.FooterErrorState
import com.thinkup.mvi.ui.shared.FooterLoadingState
import com.thinkup.mvi.ui.shared.NoInternetState
import com.thinkup.mvi.ui.shared.TkupDirectorItem
import com.thinkup.mvi.ui.shared.TkupDirectorItemConfig
import com.thinkup.mvi.ui.shared.TkupEmptyView
import com.thinkup.mvi.ui.shared.TkupEmptyViewConfig
import com.thinkup.mvi.ui.shared.TkupPagedLayoutConfig
import com.thinkup.mvi.ui.shared.TkupPagedList
import com.thinkup.mvi.ui.shared.TkupPagingItem
import com.thinkup.mvi.ui.shared.TkupPagingLayout

@Composable
fun DirectorsTab(
    viewModel: DirectorsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    if (state.noInternetClicked >= 0)
        TkupPagedList(
            modifier = Modifier
                .fillMaxSize(),
            viewModel = viewModel.pagingViewModel,
            itemType = DirectorsItemTkup(),
            layout = TkupPagedLayoutConfig(TkupPagingLayout.GRID, 2),
            emptyState = { DirectorsEmptyState() },
            noInternetState = { NoInternetState { viewModel.onReload() } },
            footerLoadingState = { FooterLoadingState() },
            footerErrorState = { FooterErrorState() }
        )
}

@Composable
fun DirectorsEmptyState() {
    TkupEmptyView(
        config = TkupEmptyViewConfig(
            message = stringResource(id = R.string.directors_empty_message),
            icon = R.drawable.tkup_empty_directors
        )
    )
}

class DirectorsItemTkup : TkupPagingItem {

    @Composable
    override fun GetItem(data: Any, index: Int, onClick: ((Any, Int) -> Unit)?) {
        when (data) {
            is Director -> TkupDirectorItem(config = TkupDirectorItemConfig(data))
        }
    }
}