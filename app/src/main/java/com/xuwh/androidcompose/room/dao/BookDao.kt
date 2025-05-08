package com.xuwh.androidcompose.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.xuwh.androidcompose.room.entity.BookEntity

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.room.dao
 * @ClassName:      BookDao
 * @Description:    小说数据库操作接口
 * @Author:         xuwh
 * @CreateDate:     2025/4/16 下午9:55
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/4/16 下午9:55
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@Dao
interface BookDao {

    @Insert
    suspend fun insertBook(bookEntity: BookEntity)

    @Query("SELECT * FROM book ")
    suspend fun queryAllBook(): List<BookEntity>?

    @Query("SELECT * FROM book WHERE book_name = :bookName")
    suspend fun queryBookByName(bookName: String): BookEntity?

}