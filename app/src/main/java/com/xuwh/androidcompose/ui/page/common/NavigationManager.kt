package com.xuwh.androidcompose.ui.page.common

import androidx.navigation.NavHostController
import com.xuwh.androidcompose.data.store.DataStoreUtil

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.ui.common
 * @ClassName:      NavigationManager
 * @Description:    类作用描述
 * @Author:         xuwh
 * @CreateDate:     2025/3/22 下午3:40
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/3/22 下午3:40
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
object NavigationManager {

    const val MAIN = "main"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val CLASSIFY = "classify"
    const val STRUCTURE = "structure"
    const val KLINE = "kline"

    const val BOOKSHELF = "book_shelf"
    const val BOOKREADER = "bookReader/{bookName}"

    const val EXAMPLE = "example"
    const val CAMERA = "camera"
    const val VIDEO = "VIDEO"


    fun getStartDestination() = if (isLogin) HOME else LOGIN

    private var isLogin: Boolean
        get() = DataStoreUtil.getBoolean("isLogin", false)
        set(value) {
            DataStoreUtil.saveBoolean("isLogin", value)
        }
}

fun NavHostController.back() {
    navigateUp()
}