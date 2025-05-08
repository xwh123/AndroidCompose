package com.xuwh.androidcompose.base.event

import com.xuwh.androidcompose.data.http.bean.Article

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.base.event
 * @ClassName:      HomeEvent
 * @Description:    首页事件
 * @Author:         xuwh
 * @CreateDate:     2025/3/27 下午10:04
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/3/27 下午10:04
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
sealed class HomeEvent : UIEvent() {
    data class UnCollectArticle(val articleId: Int) : HomeEvent()
    data class  CollectArticle(val articleId: Int) : HomeEvent()

}