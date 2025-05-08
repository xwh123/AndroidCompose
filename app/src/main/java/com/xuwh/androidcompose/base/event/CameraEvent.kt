package com.xuwh.androidcompose.base.event

import android.content.Context
import androidx.lifecycle.LifecycleOwner

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.base.event
 * @ClassName:      CameraEvent
 * @Description:    相机事件
 * @Author:         xuwh
 * @CreateDate:     2025/4/28 下午6:16
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/4/28 下午6:16
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
sealed class CameraEvent:UIEvent() {

    data class OnPermissionResult(val isGranted: Boolean):CameraEvent()
    object StartCamera : CameraEvent()
    object StopCamera : CameraEvent()

}