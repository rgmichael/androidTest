package com.br.gitreposapp.utils

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.snapshotFlow

fun LazyListState.isScrolledToEnd(buffer: Int = 1): Boolean {
    val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
    return lastVisibleItem?.index ?: 0 >= layoutInfo.totalItemsCount - buffer
}

suspend fun LazyListState.onScrollToEnd(buffer: Int = 1, onLoadMore: () -> Unit) {
    snapshotFlow { isScrolledToEnd(buffer) }
        .collect { isEnd ->
            if (isEnd) onLoadMore()
        }
}