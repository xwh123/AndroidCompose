package com.xuwh.androidcompose.ui.page.register

import com.xuwh.androidcompose.data.http.ApiService
import javax.inject.Inject

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.ui.page.register
 * @ClassName:      RegisterRepository
 * @Description:    注册
 * @Author:         xuwh
 * @CreateDate:     2025/3/24 下午9:50
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/3/24 下午9:50
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
class RegisterRepository @Inject constructor(private val api:ApiService) {

    suspend fun reigister(userName:String,password:String,repassword:String) =api.register(userName, password, repassword)

}