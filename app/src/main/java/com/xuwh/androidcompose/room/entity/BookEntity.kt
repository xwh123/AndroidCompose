package com.xuwh.androidcompose.room.entity

import android.net.Uri
import android.os.Parcelable
import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.room.entity
 * @ClassName:      BookEntity
 * @Description:    小说实体类
 * @Author:         xuwh
 * @CreateDate:     2025/4/16 下午9:45
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/4/16 下午9:45
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@Parcelize
@Entity(tableName = "book")
data class BookEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id", typeAffinity = ColumnInfo.INTEGER)
    val id: Int = 0,

    @ColumnInfo(name = "book_name", typeAffinity = ColumnInfo.TEXT)
    var bookName: String = "",

    @ColumnInfo(name = "uri", typeAffinity = ColumnInfo.TEXT)
    var uri: String = ""
) : Parcelable {
    constructor(bookName: String, uri: Uri) : this(
        bookName = bookName,
        uri =  uri.toString() // 存储为字符串
    )
}
