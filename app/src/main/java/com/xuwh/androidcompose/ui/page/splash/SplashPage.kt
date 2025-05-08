package com.xuwh.androidcompose.ui.page.splash

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.xuwh.androidcompose.R
import kotlinx.coroutines.delay

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.ui.splash
 * @ClassName:      SplashPage
 * @Description:    欢迎界面
 * @Author:         xuwh
 * @CreateDate:     2025/3/22 下午2:53
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/3/22 下午2:53
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@Composable
fun SplashPage(nextPage: () -> Unit) {

    Box(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxSize()
            .background(Color.Transparent)
        , contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.mipmap.icon_splash_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillHeight
        )

        LaunchedEffect(Unit) {
            delay(3000)
            nextPage.invoke()
        }
    }

}