package com.xuwh.androidcompose.base.state

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.base.state
 * @ClassName:      UiState
 * @Description:    基础状态管理
 * @Author:         xuwh
 * @CreateDate:     2025/3/21 下午7:14
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/3/21 下午7:14
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
sealed class UIState <out T> {
    object Idle : UIState<Nothing>()
    object Loading : UIState<Nothing>()
    data class Success<T>(val data: T) : UIState<T>()
    object Empty : UIState<Nothing>()

}