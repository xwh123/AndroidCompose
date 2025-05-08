package com.xuwh.androidcompose.ui.page.book

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.xuwh.androidcompose.base.event.BookShelfEvent
import com.xuwh.androidcompose.ui.page.common.NavigationManager

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.ui.page.book
 * @ClassName:      BookShelfPage
 * @Description:    小说书架页面
 * @Author:         xuwh
 * @CreateDate:     2025/4/16 下午10:08
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/4/16 下午10:08
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("WrongConstant", "LaunchDuringComposition")
@Composable
fun BookShelfPage(
    nav: NavHostController, viewModel:
    BookShelfViewModel = hiltViewModel()
) {

    // 状态管理
    val books by viewModel.books.collectAsState(initial = emptyList())
    val context = LocalContext.current

    // 文件选择器启动器
    val filePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            // 解析文件名
            val fileName = getFileName(context.contentResolver, it)
            Log.d("文件路径：", it.toString())
            // 处理选中的文件
            viewModel.handleEvent(BookShelfEvent.insertBook(fileName ?: "未知文件", it))
        }
    }


    // 初始化加载数据
    LaunchedEffect(Unit) {
        viewModel.handleEvent(BookShelfEvent.QueryAllBook)
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    filePickerLauncher.launch(
                        arrayOf("text/plain"),
                    )
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "添加书籍")
            }
        }
    ) { innerPadding ->
        // 网格布局
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = innerPadding
        ) {
            items(books.size) { index ->
                val book = books[index]
                Log.d("数据路径：", book.uri)
                BookGridItem(
                    title = book.bookName,
                    onClick = {
                        nav.navigate(
                            NavigationManager.BOOKREADER
                                .replace("{bookName}", book.bookName)
                        )
                    }
                )
            }
        }
    }
}

// 网格项组件
@Composable
fun BookGridItem(title: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .aspectRatio(0.7f)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = title,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}


// 获取文件名扩展函数
@SuppressLint("Range")
fun getFileName(contentResolver: ContentResolver, uri: Uri): String? {
    return when (uri.scheme) {
        "content" -> {
            contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    cursor.getString(
                        cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    )
                } else null
            }
        }

        "file" -> uri.lastPathSegment
        else -> null
    }
}
