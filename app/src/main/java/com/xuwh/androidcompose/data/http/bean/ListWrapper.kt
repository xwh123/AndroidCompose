package com.xuwh.androidcompose.data.http.bean

import com.squareup.moshi.JsonClass

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.data.http.bean
 * @ClassName:      ListWrapper
 * @Description:    分页数据封装
 * @Author:         xuwh
 * @CreateDate:     2025/3/30 下午3:37
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/3/30 下午3:37
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@JsonClass(generateAdapter = true)
data class ListWrapper<T>(
    var curPage: Int,
    var offset: Int,
    var over: Boolean,
    var pageCount: Int,
    var size: Int,
    var total: Int,
    var datas: List<T> = emptyList(),
    var data: List<T> = emptyList()
)
