package com.xuwh.androidcompose.data.http.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.net.bean
 * @ClassName:      Banner
 * @Description:    首页轮播图
 * @Author:         xuwh
 * @CreateDate:     2025/3/18 下午2:14
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/3/18 下午2:14
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@Parcelize
data class Banner(
    val desc: String, val id: Int, val imagePath: String, val isVisible: Int, val
    order: Int, val title: String, val type: Int, val url: String
):Parcelable
