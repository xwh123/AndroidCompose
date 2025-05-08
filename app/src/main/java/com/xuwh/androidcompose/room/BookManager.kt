package com.xuwh.androidcompose.room

import android.app.Application
import androidx.room.Room
import com.xuwh.androidcompose.room.dao.BookDao
import com.xuwh.androidcompose.room.database.BookDataBase
import javax.inject.Inject
import javax.inject.Singleton

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.room
 * @ClassName:      BookManager
 * @Description:    小说数据库操作类   Application中初始化
 * @Author:         xuwh
 * @CreateDate:     2025/4/16 下午9:59
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/4/16 下午9:59
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@Singleton
class BookManager @Inject constructor(private val database: BookDataBase){

    fun getBookDao(): BookDao = database.bookDao()
}