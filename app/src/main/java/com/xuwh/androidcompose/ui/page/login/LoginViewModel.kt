package com.xuwh.androidcompose.ui.page.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewModelScope
import com.xuwh.androidcompose.base.effect.UIEffect
import com.xuwh.androidcompose.base.event.LoginEvent
import com.xuwh.androidcompose.base.event.UIEvent
import com.xuwh.androidcompose.base.state.UIState
import com.xuwh.androidcompose.base.viewmodel.BaseViewModel
import com.xuwh.androidcompose.data.http.bean.User
import com.xuwh.androidcompose.data.store.DataStoreUtil
import com.xuwh.androidcompose.ui.page.common.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.ui.page.splash.login
 * @ClassName:      LoginViewModel
 * @Description:    类作用描述
 * @Author:         xuwh
 * @CreateDate:     2025/3/22 下午6:17
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/3/22 下午6:17
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository) :
    BaseViewModel<UIState<User>, UIEvent>(UIState.Idle) {

    private val _userName = MutableStateFlow("")
    val userName = _userName.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    override fun handleEvent(event: UIEvent) {
        when (event) {
            is LoginEvent.UserNameChanged -> _userName.value = event.userName
            is LoginEvent.PasswordChanged -> _password.value = event.password
            is LoginEvent.SubmitLogin -> login()
            else -> Unit
        }

    }

    /**
     * 登录
     */
    private fun login() {
        val currentUsername = _userName.value
        val currentPassword = _password.value

        if (currentUsername.isBlank() || currentPassword.isBlank()) {

            viewModelScope.launch {
                sendEvent(UIEvent.ShowSnackbar("用户名和密码不能为空"))
            }
            return
        }

        executeRequest(request = { loginRepository.login(currentUsername, currentPassword) },
            onSuccess = {DataStoreUtil.saveBoolean("isLogin",true)})
    }
}

