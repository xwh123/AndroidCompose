package com.xuwh.androidcompose.ui.page.register

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewModelScope
import com.xuwh.androidcompose.base.event.RegisterEvent
import com.xuwh.androidcompose.base.event.UIEvent
import com.xuwh.androidcompose.base.state.UIState
import com.xuwh.androidcompose.base.viewmodel.BaseViewModel
import com.xuwh.androidcompose.data.http.bean.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.ui.page.register
 * @ClassName:      RegisterViewModel
 * @Description:    注册
 * @Author:         xuwh
 * @CreateDate:     2025/3/24 下午9:50
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/3/24 下午9:50
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@HiltViewModel
class RegisterViewModel @Inject constructor(private val registerRepository: RegisterRepository) :
    BaseViewModel<UIState<User>, UIEvent>(UIState.Idle) {

    private val _userName = MutableStateFlow("")
    val userName = _userName.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _repassword = MutableStateFlow("")
    val repassword = _repassword.asStateFlow()

    override fun handleEvent(event: UIEvent) {
        when (event) {
            is RegisterEvent.UserNameChanged -> _userName.value = event.userName
            is RegisterEvent.PasswordChanged -> _password.value = event.password
            is RegisterEvent.RePasswordChanged -> _repassword.value = event.repassword
            is RegisterEvent.SubmitRegister -> register()
            else -> Unit
        }
    }

    private fun register() {
        val currentUsername = _userName.value
        val currentPassword = _password.value
        val currentRePassword = _repassword.value

        if (currentUsername.isBlank() || currentPassword.isBlank() || currentRePassword.isBlank()) {

            viewModelScope.launch {
                sendEvent(UIEvent.ShowSnackbar("用户名和密码、确认密码不能为空"))
            }
            return
        }

        executeRequest(request = {
            registerRepository.reigister(
                currentUsername, currentPassword, currentRePassword
            )
        })
    }
}