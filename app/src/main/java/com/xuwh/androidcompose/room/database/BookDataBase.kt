package com.xuwh.androidcompose.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.xuwh.androidcompose.room.dao.BookDao
import com.xuwh.androidcompose.room.entity.BookEntity

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.room.database
 * @ClassName:      BookDataBase
 * @Description:    小说DataBase
 * @Author:         xuwh
 * @CreateDate:     2025/4/16 下午9:53
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/4/16 下午9:53
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@Database(
    entities = [BookEntity::class],
    version = 1,
    exportSchema = false // 关闭架构导出简化问题排查
)
abstract class BookDataBase : RoomDatabase() {

    abstract fun bookDao(): BookDao
}