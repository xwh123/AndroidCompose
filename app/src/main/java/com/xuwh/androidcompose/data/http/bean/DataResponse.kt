package com.xuwh.androidcompose.data.http.bean

import com.squareup.moshi.JsonClass

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.net
 * @ClassName:      DataResponse
 * @Description:    数据返回类
 * @Author:         xuwh
 * @CreateDate:     2025/3/17 下午3:26
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/3/17 下午3:26
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@JsonClass(generateAdapter = true)
data class DataResponse<T>(var data:T?,var errorCode:Int,var errorMsg:String)
