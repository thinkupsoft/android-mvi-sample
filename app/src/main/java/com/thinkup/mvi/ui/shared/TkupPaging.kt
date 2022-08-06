package com.thinkup.mvi.ui.shared

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.cachedIn
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.thinkup.mvi.R
import com.thinkup.mvi.ui.theme.DIMEN_MEDIUM
import com.thinkup.mvi.ui.theme.DIMEN_MIN_SPACE
import com.thinkup.mvi.ui.theme.DIMEN_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_XXX_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_XX_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_X_SMEDIUM
import com.thinkup.mvi.ui.theme.Gray2
import com.thinkup.mvi.ui.theme.PrimaryDark
import com.thinkup.mvi.ui.theme.Typography
import com.thinkup.mvi.ui.theme.inputText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class TkupPagingViewModel @Inject constructor(
    private val paging: TkupPaging
) : ViewModel() {

    fun init(options: Options = Options.default()): Flow<PagingData<Any>> {
        return paging.getPage(options).cachedIn(viewModelScope)
    }
}

interface Options {
    companion object {
        const val LIMIT_PAGE = 10

        fun default() = object : Options {
            override fun getPageLimit() = LIMIT_PAGE
            override fun getFilters() = hashMapOf<String, String>()
        }
    }

    fun getPageLimit(): Int

    fun getFilters(): HashMap<String, String>
}

abstract class TkupPaging {
    abstract fun getPage(options: Options): Flow<PagingData<Any>>
}

abstract class TkupCustomPaging : PagingSource<Int, Any>() {
    override fun getRefreshKey(state: PagingState<Int, Any>): Int? = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Any> {
        return try {
            var page = params.key ?: 1
            page = if (page < 1) 1 else page
            val userList = getPage(page)
            LoadResult.Page(
                data = userList,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (userList.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    abstract suspend fun getPage(nextPage: Int): List<Any>
}

interface TkupPagingItem {
    @Composable
    fun GetItem(data: Any, index: Int, onClick: ((Any, Int) -> Unit)?)

    fun getSpan(data: Any, index: Int): Int = 1
}

data class TkupPagedLayoutConfig(
    val type: TkupPagingLayout = TkupPagingLayout.LIST,
    val columnsCount: Int = 2
)

enum class TkupPagingLayout { LIST, GRID }

@Composable
fun TkupPagedList(
    viewModel: TkupPagingViewModel,
    itemType: TkupPagingItem,
    layout: TkupPagedLayoutConfig = TkupPagedLayoutConfig(),
    options: Options = Options.default(),
    modifier: Modifier = Modifier,
    onClick: ((Any, Int) -> Unit)? = null,
    emptyState: @Composable () -> Unit = { NoMatches() },
    noInternetState: @Composable () -> Unit = { NoInternet() },
    footerLoadingState: @Composable () -> Unit = { DefaultFooterLoading() },
    footerErrorState: @Composable () -> Unit = { DefaultFooterError() }
) {
    TkupInfoList(
        userList = viewModel.init(options),
        itemType = itemType,
        layout = layout,
        modifier = modifier,
        onClick = onClick,
        emptyState = emptyState,
        noInternetState = noInternetState,
        footerLoadingState = footerLoadingState,
        footerErrorState = footerErrorState
    )
}

@Composable
fun TkupInfoList(
    userList: Flow<PagingData<Any>>,
    itemType: TkupPagingItem,
    layout: TkupPagedLayoutConfig,
    modifier: Modifier = Modifier,
    onClick: ((Any, Int) -> Unit)?,
    emptyState: @Composable () -> Unit,
    noInternetState: @Composable () -> Unit,
    footerLoadingState: @Composable () -> Unit,
    footerErrorState: @Composable () -> Unit
) {
    val itemsListItems: LazyPagingItems<Any> = userList.collectAsLazyPagingItems()
    val state = rememberLazyGridState()
    val config = LocalConfiguration.current

    SwipeRefresh(
        modifier = modifier,
        state = SwipeRefreshState(false),
        onRefresh = { itemsListItems.refresh() },
    ) {
        if (layout.type == TkupPagingLayout.GRID) LazyVerticalGrid(
            state = state,
            modifier = modifier,
            columns = GridCells.Fixed(layout.columnsCount),
            content = {
                val span: (LazyGridItemSpanScope.(Int) -> GridItemSpan) = { index ->
                    itemsListItems[index]?.let {
                        GridItemSpan(itemType.getSpan(it, index))
                    } ?: run {
                        GridItemSpan(1)
                    }
                }

                items(itemsListItems.itemCount, span = span) { index ->
                    itemsListItems[index]?.let { itemType.GetItem(it, index, onClick) }
                }
                getTkupPagingContentGrid(
                    itemsListItems = itemsListItems,
                    lazyGridScope = this,
                    layout = layout,
                    height = config.screenHeightDp,
                    emptyState = emptyState,
                    noInternetState = noInternetState,
                    footerLoadingState = footerLoadingState,
                    footerErrorState = footerErrorState
                )
            }
        )
        else LazyColumn(
            modifier = modifier,
            content = {
                itemsIndexed(itemsListItems) { index, item ->
                    item?.let { itemType.GetItem(it, index, onClick) }
                }
                getTkupPagingContentList(
                    itemsListItems = itemsListItems,
                    lazyListScope = this,
                    emptyState = emptyState,
                    noInternetState = noInternetState,
                    footerLoadingState = footerLoadingState,
                    footerErrorState = footerErrorState
                )
            }
        )
    }
}

private fun getTkupPagingContentList(
    itemsListItems: LazyPagingItems<Any>,
    lazyListScope: LazyListScope,
    emptyState: @Composable () -> Unit,
    noInternetState: @Composable () -> Unit,
    footerLoadingState: @Composable () -> Unit,
    footerErrorState: @Composable () -> Unit
) {
    lazyListScope.apply {
        itemsListItems.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    //You can add modifier to manage load state when first time response page is loading
                    item {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillParentMaxSize()) {
                            TkupCustomLoader(config = TkupCustomLoaderConfig(type = Type.SOFT, backgroundColor = Color.Transparent))
                        }
                    }
                }
                loadState.refresh is LoadState.Error -> {
                    item {
                        noInternetState()
                    }
                }
                loadState.append is LoadState.Loading -> {
                    //You can add modifier to manage load state when next response page is loading
                    item {
                        footerLoadingState()
                    }
                }
                loadState.append is LoadState.Error -> {
                    //You can use modifier to show error message
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = DIMEN_X_SMEDIUM)
                                .clickable { retry() },
                            horizontalArrangement = Arrangement.Center
                        ) {
                            footerErrorState()
                        }
                    }
                }
                loadState.prepend is LoadState.Loading -> {
                    item {
                        footerLoadingState()
                    }
                }
                loadState.refresh is LoadState.NotLoading -> {
                    //You can add modifier to manage load state when first time response page is loading
                    if (loadState.append.endOfPaginationReached && itemCount == 0)
                        item {
                            emptyState()
                        }
                }
            }
        }
    }
}

private fun getTkupPagingContentGrid(
    itemsListItems: LazyPagingItems<Any>,
    lazyGridScope: LazyGridScope,
    layout: TkupPagedLayoutConfig,
    height: Int,
    emptyState: @Composable () -> Unit,
    noInternetState: @Composable () -> Unit,
    footerLoadingState: @Composable () -> Unit,
    footerErrorState: @Composable () -> Unit
) {
    lazyGridScope.apply {
        val span: (LazyGridItemSpanScope) -> GridItemSpan = { GridItemSpan(layout.columnsCount) }
        itemsListItems.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    //You can add modifier to manage load state when first time response page is loading
                    item(span = span) {
                        Box(
                            contentAlignment = Alignment.Center, modifier = Modifier
                                .height((height / 1.2).dp)
                        ) {
                            TkupCustomLoader(config = TkupCustomLoaderConfig(type = Type.SOFT, backgroundColor = Color.Transparent))
                        }
                    }
                }
                loadState.refresh is LoadState.Error -> {
                    item(span = span) {
                        noInternetState()
                    }
                }
                loadState.append is LoadState.Loading -> {
                    //You can add modifier to manage load state when next response page is loading
                    item(span = span) {
                        footerLoadingState()
                    }
                }
                loadState.append is LoadState.Error -> {
                    //You can use modifier to show error message
                    item(span = span) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = DIMEN_X_SMEDIUM)
                                .clickable { retry() },
                            horizontalArrangement = Arrangement.Center
                        ) {
                            footerErrorState()
                        }
                    }
                }
                loadState.prepend is LoadState.Loading -> {
                    item(span = span) {
                        footerLoadingState()
                    }
                }
                loadState.refresh is LoadState.NotLoading -> {
                    //You can add modifier to manage load state when first time response page is loading
                    if (loadState.append.endOfPaginationReached && itemCount == 0)
                        item(span = span) {
                            emptyState()
                        }
                }
            }
        }
    }
}

@Composable
fun DefaultFooterLoading() {
    TkupCustomLoader(config = TkupCustomLoaderConfig(type = Type.SOFT, backgroundColor = Color.Transparent))
}

@Composable
fun DefaultFooterError() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(DIMEN_XX_NORMAL),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { }) {
            Text(text = stringResource(id = R.string.retry))
            Alignment.CenterHorizontally
        }
    }
}

@Composable
fun NoInternet() {
    Text(
        stringResource(R.string.no_internet),
        color = PrimaryDark,
        style = Typography.inputText,
        modifier = Modifier.padding(
            start = DIMEN_NORMAL,
            top = DIMEN_XXX_NORMAL,
            bottom = DIMEN_XX_NORMAL
        ),
        textAlign = TextAlign.Start,
    )
    Divider(
        thickness = DIMEN_MIN_SPACE,
        color = Gray2,
        modifier = Modifier.padding(horizontal = DIMEN_MEDIUM)
    )
}

@Composable
fun NoMatches() {
    Text(
        stringResource(R.string.no_matches_search),
        color = PrimaryDark,
        style = Typography.inputText,
        modifier = Modifier.padding(
            start = DIMEN_NORMAL,
            top = DIMEN_XXX_NORMAL,
            bottom = DIMEN_XX_NORMAL
        ),
        textAlign = TextAlign.Start,
    )
    Divider(
        thickness = DIMEN_MIN_SPACE,
        color = Gray2,
        modifier = Modifier.padding(horizontal = DIMEN_MEDIUM)
    )
}