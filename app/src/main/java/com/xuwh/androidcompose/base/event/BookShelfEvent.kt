package com.xuwh.androidcompose.base.event

import android.net.Uri

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.base.event
 * @ClassName:      BookShelfEvent
 * @Description:    类作用描述
 * @Author:         xuwh
 * @CreateDate:     2025/4/16 下午10:14
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/4/16 下午10:14
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
sealed class BookShelfEvent:UIEvent() {

    object QueryAllBook:BookShelfEvent()

    data class insertBook(val bookName:String,val uri:Uri):BookShelfEvent()
}