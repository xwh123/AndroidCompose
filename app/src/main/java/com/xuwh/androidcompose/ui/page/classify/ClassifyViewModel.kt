package com.xuwh.androidcompose.ui.page.classify

import androidx.compose.runtime.mutableStateOf
import com.xuwh.androidcompose.base.event.ClassifyEvent
import com.xuwh.androidcompose.base.event.UIEvent
import com.xuwh.androidcompose.base.state.UIState
import com.xuwh.androidcompose.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.ui.page.classify
 * @ClassName:      ClassifyViewModel
 * @Description:    分类
 * @Author:         xuwh
 * @CreateDate:     2025/4/5 下午3:50
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/4/5 下午3:50
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@HiltViewModel
class ClassifyViewModel @Inject constructor() : BaseViewModel<UIState<*>, UIEvent>(UIState.Idle) {

    private val _classifyTab = mutableStateOf(ClassifyTab())
    val classifyTab = _classifyTab

    private fun updateTabSelection(index: Int) {
        _classifyTab.value = _classifyTab.value.copy(selectedTabIndex = index.coerceIn(0, 1))
    }

    override fun handleEvent(event: UIEvent) {
     when(event){
         is ClassifyEvent.SelectTab-> updateTabSelection(event.index)
//         is ClassifyEvent.PageSwiped->updateTabSelection(event.index)
         else->Unit
     }


    }
}

data class ClassifyTab(
    val selectedTabIndex: Int = 0,
//    val tabList: List<String> = listOf("体系", "导航")
    val tabList: List<String> = listOf("体系")
)

