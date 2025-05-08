package com.xuwh.androidcompose.ui.page.home

import com.xuwh.androidcompose.data.http.ApiService
import javax.inject.Inject

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.ui.page.home
 * @ClassName:      HomeRepository
 * @Description:    类作用描述
 * @Author:         xuwh
 * @CreateDate:     2025/3/27 下午10:00
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/3/27 下午10:00
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
class HomeRepository @Inject constructor(private val api: ApiService) {

    suspend fun getHomeBanner() = api.getBanner()

    suspend fun getHomeArticles(  page:Int) = api.getHomeArticle(page)

    suspend fun collectInnerArticle(id: Int) = api.collectInnerArticle(id)

    suspend fun unCollectInnerArticle(id: Int) = api.uncollectInnerArticle(id)

}