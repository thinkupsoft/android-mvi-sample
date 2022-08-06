package com.thinkup.mvi.flows.movies

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.thinkup.models.app.Actor
import com.thinkup.mvi.R
import com.thinkup.mvi.ui.shared.FooterErrorState
import com.thinkup.mvi.ui.shared.FooterLoadingState
import com.thinkup.mvi.ui.shared.NoInternetState
import com.thinkup.mvi.ui.shared.TkupActorItem
import com.thinkup.mvi.ui.shared.TkupActorItemConfig
import com.thinkup.mvi.ui.shared.TkupEmptyView
import com.thinkup.mvi.ui.shared.TkupEmptyViewConfig
import com.thinkup.mvi.ui.shared.TkupPagedLayoutConfig
import com.thinkup.mvi.ui.shared.TkupPagedList
import com.thinkup.mvi.ui.shared.TkupPagingItem
import com.thinkup.mvi.ui.shared.TkupPagingLayout

@Composable
fun ActorsTab(
    viewModel: ActorsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    if (state.noInternetClicked >= 0)
        TkupPagedList(
            modifier = Modifier
                .fillMaxSize(),
            viewModel = viewModel.pagingViewModel,
            layout = TkupPagedLayoutConfig(TkupPagingLayout.GRID, 2),
            itemType = ActorsItemTkup(),
            emptyState = { ActorsEmptyState() },
            noInternetState = { NoInternetState { viewModel.onReload() } },
            footerLoadingState = { FooterLoadingState() },
            footerErrorState = { FooterErrorState() }
        )
}

@Composable
fun ActorsEmptyState() {
    TkupEmptyView(
        config = TkupEmptyViewConfig(
            message = stringResource(id = R.string.actors_empty_message),
            icon = R.drawable.tkup_empty_actors
        )
    )
}

class ActorsItemTkup : TkupPagingItem {

    @Composable
    override fun GetItem(data: Any, index: Int, onClick: ((Any, Int) -> Unit)?) {
        when (data) {
            is Actor -> TkupActorItem(config = TkupActorItemConfig(data))
        }
    }
}