package com.xuwh.androidcompose.ui.page.classify

import androidx.lifecycle.viewModelScope
import com.xuwh.androidcompose.base.event.UIEvent
import com.xuwh.androidcompose.base.state.UIState
import com.xuwh.androidcompose.base.viewmodel.BaseViewModel
import com.xuwh.androidcompose.data.http.bean.Structure
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.ui.page.classify
 * @ClassName:      StructureViewModel
 * @Description:    类作用描述
 * @Author:         xuwh
 * @CreateDate:     2025/4/5 下午5:10
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/4/5 下午5:10
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@HiltViewModel
class StructureViewModel @Inject constructor(
    private val structureRepository:
    StructureRepository
) : BaseViewModel<UIState<*>,
        UIEvent>(UIState.Idle) {

    private val _structureList = MutableStateFlow<List<Structure>>(emptyList())
    val structure = _structureList.asStateFlow()


    override fun handleEvent(event: UIEvent) {

    }

    init {
        viewModelScope.launch {
            executeRequest(request = structureRepository::queryStructure, onSuccess = {
                _structureList.value = it
            })
        }
    }
}