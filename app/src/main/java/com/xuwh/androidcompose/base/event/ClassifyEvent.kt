package com.xuwh.androidcompose.base.event

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.base.event
 * @ClassName:      ClassifyEvent
 * @Description:    分类tab
 * @Author:         xuwh
 * @CreateDate:     2025/4/5 下午4:37
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/4/5 下午4:37
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
sealed class ClassifyEvent:UIEvent() {

    data class SelectTab(val index: Int) : ClassifyEvent()
    data class PageSwiped(val index: Int) : ClassifyEvent()

}