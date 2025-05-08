package com.xuwh.androidcompose.ui.page.home

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.xuwh.androidcompose.R
import com.xuwh.androidcompose.base.event.HomeEvent
import com.xuwh.androidcompose.base.event.UIEvent
import com.xuwh.androidcompose.data.http.bean.Article
import com.xuwh.androidcompose.ext.toDateTime
import com.xuwh.androidcompose.ui.page.common.StateHandler
import com.xuwh.androidcompose.ui.widget.ComposeBannerView
import com.xuwh.androidcompose.ui.widget.PagingList
import com.xuwh.androidcompose.ui.widget.popSnackBar
/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.ui.page.home
 * @ClassName:      HomePage
 * @Description:    首页
 * @Author:         xuwh
 * @CreateDate:     2025/3/27 下午9:14
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/3/27 下午9:14
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun HomePage(
    nav: NavHostController, snackBarHostState: SnackbarHostState, viewModel:
    HomeViewModel = hiltViewModel()
) {

    val bannerList by viewModel.bannerList.collectAsState()

    val pagingItems = viewModel.articlesFlow.collectAsLazyPagingItems()
    val listState = rememberLazyListState()

    val uiState by viewModel.uiState.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    // 事件处理
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is UIEvent.ShowSnackbar -> popSnackBar(
                    coroutineScope,
                    snackBarHostState,
                    event.message
                )

                else -> Unit
            }
        }
    }

    Scaffold(contentWindowInsets = WindowInsets(0, 0, 0, 0)) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {

                ComposeBannerView(imageUrls = bannerList, onItemClick = {})

                PagingList(
                    modifier = Modifier.padding(top = 10.dp),
                    pagingItems,
                    listState = listState,
                    onRefresh = { pagingItems.refresh() },
                ) {
                    items(count = pagingItems.itemCount, key = { index ->
                        val item = pagingItems[index]
                        item?.id ?: index
                    }) {
                        it?.let {
                            val item = pagingItems[it]
                            ArticleItem(
                                item!!,
                                viewModel = viewModel,  // 传递 ViewModel
                                index = it,
                                listState = listState
                            )
                        }
                    }
                }
            }
            StateHandler(state = uiState, modifier = Modifier.matchParentSize(), onSuccess = {})
        }

    }
}

@Composable
private fun ArticleItem(
    article: Article, viewModel: HomeViewModel,
    index: Int,  // 新增index参数
    listState: LazyListState  // 新增列表状态参数
) {

    // 从 ViewModel 获取最新收藏状态
    val collectStates by viewModel.collectStates.collectAsState()
    val isCollected = collectStates[article.id] ?: article.collect

    var shouldAnimate by remember { mutableStateOf(false) }
    val isFirstVisible = remember { mutableStateOf(true) }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                val isVisible = visibleItems.any { it.index == index }
                if (isVisible && isFirstVisible.value) {
                    shouldAnimate = true
                    isFirstVisible.value = false
                }
            }
    }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth()
                .padding(6.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
                .clickable(
                    //去除点击的效果
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { /* 点击处理 */ }
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
            ) {
                Text(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(start = 16.dp),
                    text = article.chapterName ?: "compose",
                    fontWeight = FontWeight.Bold,
                    fontSize =
                    16.sp,
                    color = Color.Black
                )
                Text(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(start = 16.dp, top = 10.dp),
                    text = article.title ?: "compose",
                    fontStyle = FontStyle.Italic,
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, start = 16.dp, end = 20.dp),
                    verticalAlignment = Alignment.CenterVertically, // 使所有子项垂直居中[2,3](@ref)
                    horizontalArrangement = Arrangement.SpaceBetween // 两端分布空间
                )
                {
                    Text(
                        text = article.author ?: "compose", fontSize = 12.sp, color = Color.Gray,
                        // 保持自然宽度
                        modifier = Modifier.wrapContentWidth()
                    )

                    Box(
                        // 占据中间剩余空间
                        modifier = Modifier.wrapContentWidth()
                    ) {

                        Text(
                            // 文本在Box内居中
                            modifier = Modifier.align(Alignment.Center),
                            text = article.publishTime.toDateTime(), fontSize = 12.sp,
                            color = Color.Gray, textAlign = TextAlign.Center
                        )
                    }

                    CollectIcon(
                        isCollected = isCollected,
                        onCollectClick = {
                            viewModel.handleEvent(
                                if (isCollected)
                                    HomeEvent.UnCollectArticle(articleId = article.id)
                                else
                                    HomeEvent.CollectArticle(articleId = article.id)
                            )
                        }
                    )
                }
            }
        }
//    }
}

@Composable
private fun CollectIcon(isCollected: Boolean, onCollectClick: () -> Unit) {

    // 动画参数
    val color by animateColorAsState(
        targetValue = if (isCollected) Color.Red else Color.Gray,
        animationSpec = tween(300), label = ""
    )

    val scale by animateFloatAsState(
        targetValue = if (isCollected) 1.2f else 1f,
        animationSpec = keyframes {
            durationMillis = 500
            if (isCollected) {
                // 比心动画：放大->弹回
                1.5f at 100
                1.2f at 300
            } else {
                // 心碎动画：缩小+旋转
                0.8f at 100
                1f at 300
            }
        }, label = ""
    )

    val rotation by animateFloatAsState(
        targetValue = if (isCollected) 0f else -45f,
        animationSpec = tween(300), label = ""
    )

    AnimatedVisibility(visible = true, enter = fadeIn()+ scaleIn(),
        exit = fadeOut()+ scaleOut()) {
        Icon(
            painter = painterResource(
                if (isCollected) R.mipmap.icon_collected
                else R.mipmap.icon_un_collect
            ),
            contentDescription = null,
            tint = color,
            modifier = Modifier
                .graphicsLayer {
                    scaleX=scale
                    scaleY=scale
                    rotationZ=rotation
                }
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onCollectClick()
                }
        )
    }
}

