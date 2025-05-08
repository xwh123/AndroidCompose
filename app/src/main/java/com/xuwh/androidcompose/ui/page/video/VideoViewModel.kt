package com.xuwh.androidcompose.ui.page.video

import androidx.lifecycle.viewModelScope
import com.xuwh.androidcompose.base.event.UIEvent
import com.xuwh.androidcompose.base.event.VideoEvent
import com.xuwh.androidcompose.base.state.UIState
import com.xuwh.androidcompose.base.viewmodel.BaseViewModel
import com.xuwh.androidcompose.data.http.bean.Video
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.ui.page.video
 * @ClassName:      VideoViewModel
 * @Description:    视频viewmodel
 * @Author:         xuwh
 * @CreateDate:     2025/5/7 下午5:47
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/5/7 下午5:47
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
class VideoViewModel @Inject constructor() : BaseViewModel<UIState<*>, UIEvent>(UIState.Idle) {
    private val _currentVideoIndex = MutableStateFlow(0)
    val currentVideoIndex: StateFlow<Int> = _currentVideoIndex.asStateFlow()

    private val _videos = MutableStateFlow<List<Video>>(emptyList())
    val videos: StateFlow<List<Video>> = _videos.asStateFlow()

    fun setCurrentIndex(index: Int) {
        if (index in 0 until _videos.value.size) {
            _currentVideoIndex.value = index
        }
    }

    override fun handleEvent(event: UIEvent) {
        // 滑动切换不再需要手动处理
    }

    init {
        loadVideos()
    }

    private fun loadVideos() {
        viewModelScope.launch {
            setState(UIState.Loading)
            delay(1000)
            val mockVideos = listOf(
                Video(0, "https://sf1-cdn-tos.huoshanstatic" +
                        ".com/obj/media-fe/xgplayer_doc_video/mp4/xgplayer-demo-360p.mp4", "测试视频1"),
                Video(1, "https://www.w3schools.com/html/movie.mp4", "测试视频2"),
                Video(2, "https://media.w3.org/2010/05/sintel/trailer.mp4", "测试视频3"),
                Video(3, "https://stream7.iqilu" +
                        ".com/10339/upload_transcode/202002/09/20200209105011F0zPoYzHry.mp4",
                    "测试视频4"),
                Video(4, "https://stream7.iqilu" +
                        ".com/10339/upload_transcode/202002/09/20200209104902N3v5Vpxuvb.mp4",
                    "测试视频5"),
            )
            _videos.value = mockVideos
            setState(UIState.Success(mockVideos))
        }
    }
}