package com.xuwh.androidcompose.data.http

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.data.http
 * @ClassName:      HttpResult
 * @Description:    请求结果的封装类
 * @Author:         xuwh
 * @CreateDate:     2025/3/30 下午4:35
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/3/30 下午4:35
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
sealed  class HttpResult<out T> {

    data class Success<T>(val result: T): HttpResult<T>()
    data class Error(val exception: Exception): HttpResult<Nothing>()
}