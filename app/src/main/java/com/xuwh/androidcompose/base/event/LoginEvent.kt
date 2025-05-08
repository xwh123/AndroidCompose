package com.xuwh.androidcompose.base.event

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.base.event
 * @ClassName:      LoginEvent
 * @Description:    类作用描述
 * @Author:         xuwh
 * @CreateDate:     2025/3/22 下午6:35
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/3/22 下午6:35
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
sealed class LoginEvent : UIEvent() {
    data class UserNameChanged(val userName: String) : LoginEvent()
    data class PasswordChanged(val password: String) : LoginEvent()

    object SubmitLogin : LoginEvent()
}