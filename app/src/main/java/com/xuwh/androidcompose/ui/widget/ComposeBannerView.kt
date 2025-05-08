package com.xuwh.androidcompose.ui.widget

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.semantics.Role.Companion.RadioButton
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import coil.size.Size
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.absoluteValue

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.ui.widget
 * @ClassName:      ComposeBannerView
 * @Description:    自定义轮播图组件 3D画廊效果
 * @Author:         xuwh
 * @CreateDate:     2025/3/29 下午5:48
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/3/29 下午5:48
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ComposeBannerView(imageUrls: List<String>, onItemClick: (Int) -> Unit = {}) {

    // 空数据检查
    if (imageUrls.isEmpty()) return

    // 无限轮播控制参数  不能使用Int.MAX_VALUE  否则无法正常滚动  官方文档并未给出最大值的限制 理论上可以使用Int.MAX_VALUE
    // 但是实际测试中会出现无法滚动的情况   过多的分页会导致内存占用和渲染性能问题，具体阈值取决于页面内容的复杂程度和设备硬件性能
    //Compose 分页器的滑动机制默认一次快速滑动一页，若分页数量过大，可能导致滑动操作不流畅‌
    val infinitePageCount = 100

    val pagerState = rememberPagerState( pageCount = {
        infinitePageCount
    })

    var autoPlay by remember { mutableStateOf(true) }

    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()

    // 生命周期控制
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            autoPlay = when (event) {
                Lifecycle.Event.ON_RESUME -> true
                Lifecycle.Event.ON_PAUSE -> false
                else -> autoPlay
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // 自动轮播逻辑

    if (isDragged.not()){
        LaunchedEffect(autoPlay) {
            if (autoPlay && imageUrls.size > 1) {
                while (true) {
                    delay(3000)
                    val currentPage = (pagerState.currentPage % imageUrls.size) + 1
                    pagerState.animateScrollToPage(currentPage)
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .height(180.dp),
            pageSpacing = 16.dp,
            contentPadding = PaddingValues(horizontal = 40.dp)
        ) { page ->
            val actualPage = page % imageUrls.size

            // 3D画廊效果增强
            Card(
                modifier = Modifier
                    .carouselTransition(page,pagerState)
                    .clickable { onItemClick(actualPage) }
            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(10.dp)),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrls[actualPage])
                        .scale(Scale.FILL)
                        .build(),
                    contentDescription = "图片$actualPage",
                    contentScale = ContentScale.Crop
                )
            }

        }

        // 指示器
        if (imageUrls.size > 1) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(imageUrls.size) { index ->
                    val currentPage by remember {
                        derivedStateOf { pagerState.currentPage % imageUrls.size }
                    }
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(
                                color = if (currentPage == index)
                                    Color.Green
                                else
                                    Color.LightGray
                            )
                            .padding(2.dp)
                    )
                    if (index != imageUrls.lastIndex) Spacer(modifier = Modifier.width(4.dp))
                }
            }
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.carouselTransition(page: Int, pagerState: PagerState) =
    graphicsLayer {
        val pageOffset =
            ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue

        val transformation =
            lerp(
                start = 0.85f,
                stop = 1f,
                fraction = 1f - pageOffset.coerceIn(0f, 1f)
            )
        scaleY = transformation
    }
