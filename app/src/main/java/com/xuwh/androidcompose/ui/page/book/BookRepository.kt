package com.xuwh.androidcompose.ui.page.book

import android.content.Context
import android.net.Uri
import android.util.Log
import com.xuwh.androidcompose.room.dao.BookDao
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flow
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.ui.page.book
 * @ClassName:      BookRepository
 * @Description:    本地小说读取仓库
 * @Author:         xuwh
 * @CreateDate:     2025/4/15 下午10:06
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/4/15 下午10:06
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
class BookRepository @Inject constructor( private val bookDao: BookDao) {

    // 使用缓冲读取和固定块大小
    suspend fun loadLocalBook(context: Context, bookName: String): Flow<String> = flow {
        try {
            bookDao.queryBookByName(bookName)?.let { bookEntity ->
                context.contentResolver.openInputStream(Uri.parse(bookEntity.uri))?.use { stream ->
                    val reader = InputStreamReader(stream).buffered() // 添加缓冲读取
                    val buffer = CharArray(4096) // 4KB缓冲区
                    var remainingBuilder = StringBuilder()

                    while (true) {
                        val bytesRead = reader.read(buffer)
                        if (bytesRead == -1) break

                        val chunk = remainingBuilder.append(String(buffer, 0, bytesRead))

                        // 按固定块分割，减少中间对象
                        var position = 0
                        while (position + 4096 < chunk.length) {
                            emit(chunk.substring(position, position + 4096))
                            position += 4096
                        }
                        remainingBuilder = StringBuilder(chunk.substring(position))
                    }

                    // 发射剩余内容
                    if (remainingBuilder.isNotEmpty()) {
                        emit(remainingBuilder.toString())
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("BookRepository", "Load book failed", e)
            throw e
        }
    }.buffer(Channel.BUFFERED) //添加背压处理
}