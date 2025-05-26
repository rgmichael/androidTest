package com.br.gitreposapp.utils

import androidx.compose.foundation.lazy.LazyListState

fun LazyListState.isScrolledToEnd(buffer: Int = 1): Boolean {
    val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
    return lastVisibleItem?.index ?: 0 >= layoutInfo.totalItemsCount - buffer
}
