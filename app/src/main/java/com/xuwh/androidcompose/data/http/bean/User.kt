package com.xuwh.androidcompose.data.http.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.net.bean
 * @ClassName:      User
 * @Description:    用户实体类
 * @Author:         xuwh
 * @CreateDate:     2025/3/17 下午2:54
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/3/17 下午2:54
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@Parcelize
data class User(
    val admin: Boolean, val chapterTops: List<String> = mutableListOf(), val
    email: String = "", val icon: String = "",
    val id: String = "",
    val nickname: String = "",
    val password: String = "",
    val token: String = "",
    val type: Int = 0,
    val username: String = "",
    val coinCount:Int=0,
):Parcelable