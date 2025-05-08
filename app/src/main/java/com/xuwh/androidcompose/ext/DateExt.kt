package com.xuwh.androidcompose.ext

import android.icu.text.SimpleDateFormat
import java.util.Locale

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.ext
 * @ClassName:      Date
 * @Description:    日期相关扩展函数
 * @Author:         xuwh
 * @CreateDate:     2025/3/31 下午9:37
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/3/31 下午9:37
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

fun Long.toDateTime(pattern: String = "yyyy-MM-dd HH:mm:ss"): String =
    SimpleDateFormat(pattern, Locale.getDefault()).format(this)
