package com.xuwh.androidcompose.ui.page.login

import com.xuwh.androidcompose.data.http.ApiService
import com.xuwh.androidcompose.data.http.bean.DataResponse
import com.xuwh.androidcompose.data.http.bean.User
import javax.inject.Inject

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.ui.page.splash.login
 * @ClassName:      LoginRepository
 * @Description:    登录接口
 * @Author:         xuwh
 * @CreateDate:     2025/3/22 下午6:18
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/3/22 下午6:18
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
class LoginRepository @Inject constructor(private val api:ApiService) {
    suspend fun login(account: String, password: String)= api.login(account, password)
}