package com.xuwh.androidcompose.ui.page.book

import android.content.Context
import android.content.res.Resources
import android.net.Uri
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import com.xuwh.androidcompose.base.event.BookEvent
import com.xuwh.androidcompose.base.event.UIEvent
import com.xuwh.androidcompose.base.state.UIState
import com.xuwh.androidcompose.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.ui.page.book
 * @ClassName:      BookReaderViewModel
 * @Description:    阅读小说ViewModel
 * @Author:         xuwh
 * @CreateDate:     2025/4/15 下午10:13
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/4/15 下午10:13
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@HiltViewModel
class BookReaderViewModel @Inject constructor(private val bookRepository: BookRepository) :
    BaseViewModel<UIState<*>, UIEvent>(UIState.Idle) {
    // 状态管理
    private val _pages = mutableStateListOf<String>()
    val pages: List<String> get() = _pages

    // 布局参数
    var textAreaWidth by mutableFloatStateOf(0f)
        private set
    var textAreaHeight by mutableFloatStateOf(0f)
    var lineSpacing by mutableFloatStateOf(0f)
        private set

    // 分页控制
    private val isPaginating = AtomicBoolean(false)
    private var paginationJob: Job? = null

    // 文本样式
    var textSize by mutableStateOf(16f)
    val textColor = mutableStateOf(0xFF333333)
    val backgroundColor = mutableStateOf(0xFFFFFFFF)

    fun updateLayoutConfig(width: Float, height: Float, lineSpacing: Float) {
        textAreaWidth = width
        textAreaHeight = height
        this.lineSpacing = lineSpacing
        Log.d("LayoutConfig", "Width:${width}px Height:${height}px LineSpacing:${lineSpacing}px")
    }

    private fun loadBook(context: Context, bookName: String) {
        paginationJob?.cancel()
        paginationJob = viewModelScope.launch {
            bookRepository.loadLocalBook(context, bookName).collect { pageContent ->
                paginateContent(pageContent)
            }
        }
    }

    private suspend fun paginateContent(content: String) = withContext(Dispatchers.Default) {
        if (content.isEmpty() || !isPaginating.compareAndSet(false, true)) return@withContext

        try {
            require(textAreaWidth > 0 && textAreaHeight > 0) {
                "Invalid layout params: width=$textAreaWidth, height=$textAreaHeight"
            }

            val textPaint = createTextPaint()
            val layoutWidth = textAreaWidth.toInt()
            var currentPosition = 0

            while (currentPosition < content.length && isPaginating.get()) {
                val (endPosition, pageContent) = buildPageContent(content, currentPosition, textPaint, layoutWidth)
                if (pageContent.isEmpty()) break
                _pages.add(pageContent)
                currentPosition = endPosition

                if (_pages.size % 10 == 0) {
                    Log.d("Pagination", "Generated ${_pages.size} pages, progress: ${currentPosition * 100 / content.length}%")
                }
            }

        } catch (e: Exception) {
            Log.e("Pagination", "Error during pagination", e)
        } finally {
            isPaginating.set(false)
        }
    }

    fun createTextPaint(): TextPaint {
        return TextPaint().apply {
            isAntiAlias = true
            textSize = spToPx(this@BookReaderViewModel.textSize)
            Log.d("TextMetrics", "Effective text size: ${this@BookReaderViewModel.textSize}sp -> ${textSize}px")
        }
    }

    private fun spToPx(spValue: Float): Float {
        return spValue * Resources.getSystem().displayMetrics.scaledDensity
    }

    private fun buildPageContent(
        content: String,
        start: Int,
        paint: TextPaint,
        layoutWidth: Int
    ): Pair<Int, String> {

        val fontMetrics = paint.fontMetrics
        // 精确计算单行高度（包含行间距）
        val lineHeight = (fontMetrics.descent - fontMetrics.ascent) + lineSpacing

        // 创建临时布局测量实际内容
        val tempLayout = StaticLayout.Builder.obtain(content, start, content.length, paint, layoutWidth)
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .setLineSpacing(lineSpacing, 1.0f)
            .setIncludePad(false) // 设置为 false 以减少额外的空白
            .build()

        // 动态计算最大可见行数
        var visibleLines = 0
        var accumulatedHeight = 0f

        while (visibleLines < tempLayout.lineCount) {
            val newHeight = accumulatedHeight + lineHeight
            if (newHeight > textAreaHeight) break
            accumulatedHeight = newHeight
            visibleLines++
        }

        if (visibleLines == 0) return start to ""

        // 创建最终布局进行精确测量
        val finalLayout = StaticLayout.Builder.obtain(content, start, content.length, paint, layoutWidth)
            .setMaxLines(visibleLines)
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .setLineSpacing(lineSpacing, 1.0f)
            .setBreakStrategy(Layout.BREAK_STRATEGY_HIGH_QUALITY)
            .setHyphenationFrequency(Layout.HYPHENATION_FREQUENCY_NORMAL)
            .setIncludePad(false) // 设置为 false 以减少额外的空白
            .build()

        var endPos = finalLayout.getLineEnd(visibleLines - 1).coerceAtMost(content.length)

        // 智能换行优化
        if (endPos < content.length) {
            val nextLineStart = finalLayout.getLineStart(visibleLines.coerceAtMost(finalLayout.lineCount - 1))
            when {
                // 情况1：当前行尾是连字符
                content[endPos - 1] == '-' -> endPos++

                // 情况2：下一行开始是标点符号
                isValidStartChar(content[nextLineStart]) -> endPos = nextLineStart

                // 情况3：检查是否需要保持单词完整
                else -> {
                    val lastSpace = content.lastIndexOf(' ', endPos)
                    if (lastSpace > start && !isValidEndChar(content[lastSpace - 1])) {
                        endPos = lastSpace + 1
                    }
                }
            }
        }

        endPos = endPos.coerceAtMost(content.length)
        return endPos to content.substring(start, endPos)
    }

    private fun isValidEndChar(char: Char): Boolean {
        return char in setOf('，', '。', '！', '？', '；', '：', ',', '.', '!', '?', ';', ':')
    }

    private fun isValidStartChar(char: Char): Boolean {
        return char in setOf('“', '‘', '(', '[', '{')
    }

    fun cancelPaginating() {
        isPaginating.set(false)
        paginationJob?.cancel()
        Log.d("Pagination", "Cancelled pagination, generated ${_pages.size} pages")
    }

    override fun handleEvent(event: UIEvent) {
        when (event) {
            is BookEvent.LoadBook -> loadBook(event.context, event.bookName)
            else -> Unit
        }
    }
}