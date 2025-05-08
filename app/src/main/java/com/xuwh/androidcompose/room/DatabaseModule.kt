package com.xuwh.androidcompose.room

import android.app.Application
import androidx.room.Room
import com.xuwh.androidcompose.room.dao.BookDao
import com.xuwh.androidcompose.room.database.BookDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.room
 * @ClassName:      DatabaseModule
 * @Description:    类作用描述
 * @Author:         xuwh
 * @CreateDate:     2025/4/16 下午10:47
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/4/16 下午10:47
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@Module
@InstallIn(SingletonComponent::class)
object  DatabaseModule {

    @Provides
    @Singleton
    fun provideBookDatabase(
        application: Application
    ): BookDataBase = Room.databaseBuilder(
        application,
        BookDataBase::class.java,
        "book.db"
    ).build()

    @Provides
    fun provideBookDao(database: BookDataBase)= database.bookDao()

}