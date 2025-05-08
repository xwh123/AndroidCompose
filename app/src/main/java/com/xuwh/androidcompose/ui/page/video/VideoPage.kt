package com.xuwh.androidcompose.ui.page.video

import androidx.compose.animation.core.Spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.wear.compose.material.Text
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.VerticalPager
import com.google.accompanist.pager.rememberPagerState

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.ui.page.video
 * @ClassName:      VideoPage
 * @Description:    类作用描述
 * @Author:         xuwh
 * @CreateDate:     2025/5/7 下午7:33
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/5/7 下午7:33
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
fun VideoPage(
    modifier: Modifier = Modifier,
    viewModel: VideoViewModel = hiltViewModel()
) {
    val videos by viewModel.videos.collectAsState()
    val pagerState = rememberPagerState()
    val currentPage by remember { derivedStateOf { pagerState.currentPage } }

    LaunchedEffect(currentPage) {
        viewModel.setCurrentIndex(currentPage)
    }

    LaunchedEffect(viewModel.currentVideoIndex) {
        if (viewModel.currentVideoIndex.value != currentPage) {
            pagerState.scrollToPage(viewModel.currentVideoIndex.value)
        }
    }

    VerticalPager(
        count = videos.size,
        state = pagerState,
        modifier = modifier.fillMaxSize(),
    ) { page ->
        val video = videos[page]
        val context = LocalContext.current
        val player = remember(page) {
            ExoPlayer.Builder(context).build().apply {
                setMediaItem(MediaItem.fromUri(video.videoUrl))
               repeatMode=ExoPlayer.REPEAT_MODE_ONE
                prepare()
            }
        }

        DisposableEffect(Unit) {
            onDispose {
                player.release()
            }
        }

        LaunchedEffect(currentPage == page) {
            if (currentPage == page) {
                player.play()
            } else {
                player.pause()
            }
        }

        VideoPlayerItem(
            player = player,
            title = video.title,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
private fun VideoPlayerItem(
    player: ExoPlayer,
    title: String,
    modifier: Modifier = Modifier
) {
    var isPlaying by remember { mutableStateOf(true) }

    Box(modifier = modifier) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    this.player = player
                    hideController()
                    useController = false
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .clickable { isPlaying = !isPlaying }
        )

        Text(
            text = title,
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }

    LaunchedEffect(isPlaying) {
        if (isPlaying) player.play() else player.pause()
    }
}

