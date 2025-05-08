package com.xuwh.androidcompose.base.event

import org.checkerframework.checker.guieffect.qual.UI

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.base.event
 * @ClassName:      VideoEvent
 * @Description:    视频播放事件
 * @Author:         xuwh
 * @CreateDate:     2025/5/7 下午5:44
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/5/7 下午5:44
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
sealed class VideoEvent:UIEvent() {

    object LoadNextVideo : VideoEvent()
    object LoadPreviousVideo : VideoEvent()
}