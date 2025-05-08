package com.xuwh.androidcompose.ui.page.book

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.xuwh.androidcompose.R
import com.xuwh.androidcompose.base.event.BookEvent

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.ui.page.book
 * @ClassName:      BookReaderPage
 * @Description:    小说阅读界面
 * @Author:         xuwh
 * @CreateDate:     2025/4/15 下午10:20
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/4/15 下午10:20
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
fun BookReaderPage(
    nav: NavHostController, bookName: String, viewModel: BookReaderViewModel =
        hiltViewModel()
) {

    val pagerState = rememberPagerState()
    val density = LocalDensity.current

    val context = LocalContext.current

    // 初始化加载数据
    LaunchedEffect(bookName) {
        viewModel.handleEvent(BookEvent.LoadBook(bookName, context))
    }

    Column {
        // 顶部导航栏
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(viewModel.backgroundColor.value)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 返回按钮
            Image(
                painter = painterResource(id = R.drawable.ic_back), // 替换为你的返回图标资源
                contentDescription = "返回",
                modifier = Modifier
                    .size(48.dp)
                    .clickable { nav.navigateUp() }
                    .padding(8.dp),
                colorFilter = ColorFilter.tint(Color(viewModel.textColor.value))
            )

            // 标题
            Text(
                text = bookName,
                color = Color(viewModel.textColor.value),
                fontSize = 18.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 48.dp)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )

        }

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(viewModel.backgroundColor.value))
        ) {
            // 使用 with(density) 作用域处理所有单位转换
            val (textAreaWidth, textAreaHeight) = with(density) {
                val widthPx = maxWidth.toPx()
                val heightPx = maxHeight.toPx()
                widthPx to heightPx
            }

            // 更新分页参数
            LaunchedEffect(textAreaWidth, textAreaHeight) {

                val verticalPaddingPx = with(density) { 16.dp.toPx() } // 上下各16dp
                val lineSpacingPx = with(density) { 4.dp.toPx() }

                viewModel.updateLayoutConfig(
                    width = textAreaWidth - with(density) { 32.dp.toPx() }, // 左右各16dp
                    height = textAreaHeight - verticalPaddingPx * 2, // 上下各16dp
                    lineSpacing = lineSpacingPx
                )
                Log.d(
                    "LayoutParams", "Updated params: " +
                            "width=$textAreaWidth, height=$textAreaHeight, " +
                            "textSize=${viewModel.textSize.sp}sp"
                )
            }

            HorizontalPager(
                count = viewModel.pages.size,
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) { page ->
                Text(
                    text = viewModel.pages.getOrNull(page) ?: "",
                    modifier = Modifier.fillMaxSize(),
                    color = Color(viewModel.textColor.value),
                    fontSize = viewModel.textSize.sp,
                    lineHeight = with(LocalDensity.current) {
                        val fontMetrics = viewModel.createTextPaint().fontMetrics
                        val lineHeight =
                            (fontMetrics.descent - fontMetrics.ascent) + viewModel.lineSpacing
                        lineHeight.toSp()
                    }
                )
            }
        }
    }

    // 处理页面离开时的资源释放
    DisposableEffect(Unit) {
        onDispose {
            viewModel.cancelPaginating()
        }
    }
}