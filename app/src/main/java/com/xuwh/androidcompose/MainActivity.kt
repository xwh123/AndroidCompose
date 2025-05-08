package com.xuwh.androidcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.xuwh.androidcompose.theme.AndroidComposeTheme
import com.xuwh.androidcompose.ui.page.common.AppNavigation
import com.xuwh.androidcompose.ui.page.splash.SplashPage
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            var isSplash by remember { mutableStateOf(true) }

            AndroidComposeTheme {
                enableEdgeToEdge()

                Box(
                    modifier = Modifier
                        .fillMaxSize()// 确保内容不会被状态栏遮挡
                ) {
                    if (isSplash) {
                        SplashPage { isSplash = false }
                    } else {
                        AppNavigation()
                    }
                }

            }
        }
    }
}

