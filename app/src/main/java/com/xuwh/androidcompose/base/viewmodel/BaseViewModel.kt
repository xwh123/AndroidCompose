package com.xuwh.androidcompose.base.viewmodel

import android.os.Debug
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xuwh.androidcompose.base.effect.UIEffect
import com.xuwh.androidcompose.base.event.UIEvent
import com.xuwh.androidcompose.base.state.UIState
import com.xuwh.androidcompose.data.http.bean.DataResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope.coroutineContext
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.base.viewmodel
 * @ClassName:      BaseViewModel
 * @Description:    基础ViewModel
 * @Author:         xuwh
 * @CreateDate:     2025/3/21 下午7:16
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/3/21 下午7:16
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
abstract class BaseViewModel<STATE : UIState<*>, EVENT : UIEvent>(
    initialState: STATE
) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<STATE> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<EVENT>()
    val events = _events.asSharedFlow()

    protected fun setState(newState: STATE) {
        _uiState.value = newState
    }

    protected suspend fun sendEvent(event: EVENT) {
        _events.emit(event)
    }


    abstract fun handleEvent(event: EVENT)

    /**
     * 执行网络请求
     * @param request SuspendFunction0<T>
     * @param onSuccess Function1<T, Unit>
     * @param showLoading Boolean
     * @param emptyCheck Function1<T, Boolean>
     */
    protected fun <T> executeRequest(
        request: suspend () -> DataResponse<T>,
        onSuccess: (T) -> Unit = {},
        showLoading: Boolean = true,
        onError: (Throwable) -> Unit = {},
        emptyCheck: (DataResponse<T>) -> Boolean = { data ->
            data is List<*> && data.isEmpty()
        }
    ) {

        val traceName = "NetworkRequest:${this::class.simpleName}"
        Debug.startMethodTracing(traceName)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (showLoading) {
                    withContext(Dispatchers.Main) {
                        setState(UIState.Loading as STATE)
                    }
                }
                val result = request()
                withContext(Dispatchers.Main) {
                    when {
                        0 != result.errorCode -> {
                            setState(UIState.Idle as STATE)
                            onError(Throwable(result.errorMsg))
                            sendEvent(UIEvent.ShowSnackbar(result.errorMsg ?: "请求错误") as EVENT)
                        }

                        emptyCheck(result) -> setState(UIState.Empty as STATE)
                        else -> {
                            setState(UIState.Success(result.data) as STATE)
                            onSuccess(result.data!!)
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError(Throwable(e))
                    setState(UIState.Idle as STATE)
                    sendEvent(UIEvent.ShowSnackbar(e.message ?: "请求错误") as EVENT)
                }
            }
        }

        Debug.stopMethodTracing()
    }

    // 在 BaseViewModel 中添加
    override fun onCleared() {
        viewModelScope.cancel()
        coroutineContext.cancel()
        super.onCleared()
    }
}
