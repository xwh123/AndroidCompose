package com.xuwh.androidcompose.ui.page.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.xuwh.androidcompose.base.event.HomeEvent
import com.xuwh.androidcompose.base.event.UIEvent
import com.xuwh.androidcompose.base.state.UIState
import com.xuwh.androidcompose.base.viewmodel.BaseViewModel
import com.xuwh.androidcompose.data.http.ApiService
import com.xuwh.androidcompose.data.http.bean.Article
import com.xuwh.androidcompose.paging.MyPagingConfig
import com.xuwh.androidcompose.paging.pagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.ui.page.home
 * @ClassName:      HomeViewModel
 * @Description:    类作用描述
 * @Author:         xuwh
 * @CreateDate:     2025/3/27 下午9:15
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/3/27 下午9:15
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@HiltViewModel
class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository) :
    BaseViewModel<UIState<*>, UIEvent>(UIState.Idle) {

    private val _bannerList = MutableStateFlow<List<String>>(emptyList())
    val bannerList = _bannerList.asStateFlow()

    // 使用 StateFlow 管理收藏状态
    private val _collectStates = MutableStateFlow<Map<Int, Boolean>>(emptyMap())
    val collectStates = _collectStates.asStateFlow()

    // 分页数据流
    //使用扩展函数：
    val articlesFlow by lazy {
        pagingSource {
            homeRepository.getHomeArticles(it)
        }.map { pagingData ->
            pagingData.map { article ->
                // 同步服务器的最新状态到本地
                _collectStates.value += (article.id to article.collect)
                article
            }
        }.cachedIn(viewModelScope)
    }

    override fun handleEvent(event: UIEvent) {
        when (event) {
            is HomeEvent.CollectArticle -> collectArticle(event.articleId)
            is HomeEvent.UnCollectArticle -> unCollectArticle(event.articleId)
            else -> Unit
        }
    }

    init {
        executeRequest(request = homeRepository::getHomeBanner,
            onSuccess = { banners ->
                _bannerList.value = banners.map { it.imagePath }
            })
    }

    private fun unCollectArticle(articleId: Int) {

        executeRequest(
            request = { homeRepository.unCollectInnerArticle(articleId) },
            onSuccess = {
                // 仅在成功时更新状态
                _collectStates.update { it + (articleId to false) }
            },
        )
    }

    private fun collectArticle(articleId: Int) {
        executeRequest(
            request = { homeRepository.collectInnerArticle(articleId) },
            onSuccess = {
                // 仅在成功时更新状态
                _collectStates.update { it + (articleId to true) }
            }
        )
    }
}
