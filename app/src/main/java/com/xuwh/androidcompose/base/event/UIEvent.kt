package com.xuwh.androidcompose.base.event

import androidx.compose.material3.SnackbarDuration

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.base.event
 * @ClassName:      UIEvent
 * @Description:    事件封装
 * @Author:         xuwh
 * @CreateDate:     2025/3/22 下午1:30
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/3/22 下午1:30
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
sealed class UIEvent {
    object Idle : UIEvent()
    data class ShowSnackbar(
        val message: String,
        val duration: SnackbarDuration = SnackbarDuration.Short
    ) : UIEvent()
}