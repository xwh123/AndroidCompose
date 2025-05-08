package com.xuwh.androidcompose.ui.page.mine

import com.xuwh.androidcompose.base.event.UIEvent
import com.xuwh.androidcompose.base.state.UIState
import com.xuwh.androidcompose.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.ui.page.mine
 * @ClassName:      MineViewModel
 * @Description:    类作用描述
 * @Author:         xuwh
 * @CreateDate:     2025/4/15 下午9:48
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/4/15 下午9:48
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@HiltViewModel
class MineViewModel @Inject constructor():BaseViewModel<UIState<*>,UIEvent>(UIState.Idle) {
    override fun handleEvent(event: UIEvent) {
    }
}