package com.xuwh.androidcompose.ui.page.kline

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.xuwh.androidcompose.base.event.UIEvent
import com.xuwh.androidcompose.base.state.UIState
import com.xuwh.androidcompose.base.viewmodel.BaseViewModel
import com.xuwh.androidcompose.data.http.bean.KLineData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.ui.page.kline
 * @ClassName:      KlineViewModel
 * @Description:    K线图ViewModel
 * @Author:         xuwh
 * @CreateDate:     2025/4/8 下午9:47
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/4/8 下午9:47
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@HiltViewModel
class KlineViewModel @Inject constructor() : BaseViewModel<UIState<*>, UIEvent>(UIState.Loading) {

    private val _klineDatas = MutableLiveData<List<KLineData>>(emptyList())

    val klineDatas = _klineDatas

    override fun handleEvent(event: UIEvent) {
    }

//    init {
//        fetchKlineData()
//    }
//
//    private fun fetchKlineData() {
//        viewModelScope.launch {
//            // 开始加载状态
//            setState(UIState.Loading as UIState<*>)
//
//            try {
//                getStockDataFlow().collect { data ->
//                    _klineDatas.value = data
//                    setState(UIState.Success(null) as UIState<*>)
//                }
//            } catch (e: Exception) {
//                e.message?.let { Log.e("TAG", it) }
//            }
//        }
//    }

    // 模拟数据获取函数
//    fun getStockDataFlow(): Flow<List<KLineData>> = flow {
//        val mockData = listOf(
//            KLineData("09-07 12:40", 4724.31f, 4744.97f, 4705.24f, 4714.39f, 72.66f),
//            KLineData("09-07 12:41", 4715.0f, 4730.0f, 4710.0f, 4720.0f, 68.0f),
//            KLineData("09-07 12:42", 4722.0f, 4735.0f, 4720.0f, 4730.0f, 70.0f),
//            KLineData("09-07 12:43", 4732.0f, 4745.0f, 4730.0f, 4740.0f, 75.0f),
//            KLineData("09-07 12:44", 4742.0f, 4755.0f, 4740.0f, 4750.0f, 80.0f),
//            KLineData("09-07 12:45", 4752.0f, 4765.0f, 4750.0f, 4760.0f, 85.0f),
//            KLineData("09-07 12:46", 4762.0f, 4775.0f, 4760.0f, 4770.0f, 90.0f),
//            KLineData("09-07 12:47", 4772.0f, 4785.0f, 4770.0f, 4780.0f, 95.0f),
//            KLineData("09-07 12:48", 4782.0f, 4795.0f, 4780.0f, 4790.0f, 100.0f),
//            KLineData("09-07 12:49", 4792.0f, 4805.0f, 4790.0f, 4800.0f, 105.0f)
//        )
//        delay(1000) // 模拟网络延迟
//        emit(mockData)
//    }
}