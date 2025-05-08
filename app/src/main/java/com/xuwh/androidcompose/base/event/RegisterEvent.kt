package com.xuwh.androidcompose.base.event

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.base.event
 * @ClassName:      RegisterEvent
 * @Description:    注册事件
 * @Author:         xuwh
 * @CreateDate:     2025/3/24 下午9:57
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/3/24 下午9:57
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
sealed class RegisterEvent : UIEvent() {

    data class UserNameChanged(val userName: String) : RegisterEvent()
    data class PasswordChanged(val password: String) : RegisterEvent()
    data class RePasswordChanged(val repassword: String) : RegisterEvent()

    object SubmitRegister : RegisterEvent()
}