package com.xuwh.androidcompose.base.event

import android.content.Context
import android.net.Uri

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.base.event
 * @ClassName:      BookEvent
 * @Description:    小说事件
 * @Author:         xuwh
 * @CreateDate:     2025/4/16 下午9:30
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/4/16 下午9:30
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
sealed class BookEvent:UIEvent() {

    data class LoadBook(val bookName: String,val context: Context):BookEvent()
}