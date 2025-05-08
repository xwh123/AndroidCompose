package com.xuwh.androidcompose.ui.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.ui.widget
 * @ClassName:      PagingList
 * @Description:    刷新加载更多的列表控件
 * @Author:         xuwh
 * @CreateDate:     2025/3/30 下午2:26
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/3/30 下午2:26
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@Composable
fun <T : Any> PagingList(
    modifier: Modifier = Modifier,
    lazyPagingItems: LazyPagingItems<T>, listState: LazyListState,
    onRefresh: ()
    -> Unit = { lazyPagingItems.refresh() },
    itemContent: LazyListScope.() -> Unit
) {

    val isRefreshing by remember {
        derivedStateOf {
            lazyPagingItems.loadState.refresh is LoadState.Loading
        }
    }

    SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing), onRefresh = onRefresh) {
        LazyColumn(
            state = listState, modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            itemContent()

            when (lazyPagingItems.loadState.append) {
                is LoadState.Loading -> item { LoadingItem() }
                is LoadState.Error -> item {
                    ErrorItem { ((lazyPagingItems.loadState.append as
                            LoadState.Error).error) }
                }
                is LoadState.NotLoading->{
                    if (0==lazyPagingItems.itemCount){
                        item { EmptyState() }
                    }else{
                        item { noMoreContent()}
                    }
                }
            }

        }
    }
}

@Composable
private fun LoadingItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            strokeWidth = 2.dp,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text("加载中...")
    }
}

@Composable
fun ErrorItem(onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onRetry() }
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Filled.Warning,
            contentDescription = "错误",
            tint = MaterialTheme.colorScheme.error
        )
        Text("加载失败，点击重试", color = MaterialTheme.colorScheme.error)
    }
}

@Composable
fun noMoreContent(){
    Box(modifier = Modifier.fillMaxWidth()) {
        Text("没有更多内容", modifier = Modifier.padding(16.dp))
    }
}


// 新增默认空状态组件
@Composable
fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Filled.Info,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "暂无数据",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}