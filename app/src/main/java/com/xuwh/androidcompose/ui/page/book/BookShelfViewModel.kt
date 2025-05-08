package com.xuwh.androidcompose.ui.page.book

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.xuwh.androidcompose.base.event.BookShelfEvent
import com.xuwh.androidcompose.base.event.UIEvent
import com.xuwh.androidcompose.base.state.UIState
import com.xuwh.androidcompose.base.viewmodel.BaseViewModel
import com.xuwh.androidcompose.room.entity.BookEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.ui.page.book
 * @ClassName:      BookShelfViewModel
 * @Description:    书架页面viewmodel
 * @Author:         xuwh
 * @CreateDate:     2025/4/16 下午9:34
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/4/16 下午9:34
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@HiltViewModel
class BookShelfViewModel @Inject constructor(
    private val bookShelfRepository:
    BookShelfRepository
) : BaseViewModel<UIState<*>,
        UIEvent>(UIState.Idle) {

    // 书籍列表
    private val _books = MutableStateFlow<List<BookEntity>>(emptyList())
    val books = _books.asStateFlow()

    override fun handleEvent(event: UIEvent) {
        when (event) {
            is BookShelfEvent.QueryAllBook -> queryAllBook()
            is BookShelfEvent.insertBook -> insertBook(event.bookName, event.uri)
            else -> Unit
        }
    }

    /**
     * 查询所有书籍
     */
    private fun queryAllBook() {
        viewModelScope.launch {

            bookShelfRepository.queryAllBook()?.let {
                _books.value = it
            }
        }
    }

    /**
     * 选择书籍
     */
    private fun insertBook(bookName: String, uri: Uri) {

        viewModelScope.launch {
            bookShelfRepository.addBook(BookEntity(bookName, uri))
            queryAllBook()
        }
    }
}