package com.xuwh.androidcompose.ui.widget

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.ui.widget
 * @ClassName:      SnackBar
 * @Description:    自定义 SnackBar  居中显示
 * @Author:         xuwh
 * @CreateDate:     2025/3/22 下午5:49
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/3/22 下午5:49
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

@Composable
fun CenterSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    snackbar: @Composable (SnackbarData) -> Unit = { CenterSnackbar(it) }
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        SnackbarHost(
            hostState = hostState,
            snackbar = snackbar,
            modifier = Modifier
                .wrapContentWidth()
                .align(Alignment.Center)
        )
    }
}


@Composable
fun CenterSnackbar(data: SnackbarData) {

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)  // 添加外层间距
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(24.dp),
                    clip = true
                ),
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(24.dp),
            tonalElevation = 2.dp
        ) {
            Text(
                text = data.visuals.message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .widthIn(min = 80.dp, max = 280.dp),  // 宽度约束
                textAlign = TextAlign.Center  // 文字居中
            )
        }
    }
}

/**
 * @Description:    显示 SnackBar
 * @param scope CoroutineScope
 * @param snackBarHostState SnackbarHostState
 * @param message String
 */
fun popSnackBar(scope: CoroutineScope, snackBarHostState: SnackbarHostState, message: String) {
    scope.launch {
        snackBarHostState.showSnackbar(message)
        delay(3000)
        snackBarHostState.currentSnackbarData?.dismiss()
    }
}