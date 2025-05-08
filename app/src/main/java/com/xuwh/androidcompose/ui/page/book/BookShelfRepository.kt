package com.xuwh.androidcompose.ui.page.book

import com.xuwh.androidcompose.room.BookManager
import com.xuwh.androidcompose.room.dao.BookDao
import com.xuwh.androidcompose.room.entity.BookEntity
import javax.inject.Inject

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.ui.page.book
 * @ClassName:      BookShelfRepository
 * @Description:    小说书架数据源
 * @Author:         xuwh
 * @CreateDate:     2025/4/16 下午10:03
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/4/16 下午10:03
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
class BookShelfRepository @Inject constructor( private val bookDao: BookDao) {

    /**
     * 查询所有书籍
     * @return List<BookEntity>?
     */
    suspend fun queryAllBook():List<BookEntity>?{
        return bookDao.queryAllBook()
    }

    /**
     * 添加数据
     */
    suspend fun addBook(bookEntity: BookEntity){
        if (null==bookDao.queryBookByName(bookEntity.bookName)){
            bookDao.insertBook(bookEntity)
        }
    }
}