package com.xuwh.androidcompose.ui.page.example

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.xuwh.androidcompose.ui.page.common.NavigationManager

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.ui.page.example
 * @ClassName:      ExamplePage
 * @Description:    示例页面
 * @Author:         xuwh
 * @CreateDate:     2025/4/26 下午2:57
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/4/26 下午2:57
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@Composable
fun ExamplePage(nav:NavHostController) {


    Column {
        Text(text = "TensorFlow 图片分类识别", modifier = Modifier.clickable {
            nav.navigate(NavigationManager.CAMERA)
        }.padding(10.dp))

        Text(text = "抖音上下滑动切换视频", modifier = Modifier.clickable {
            nav.navigate(NavigationManager.VIDEO)
        }.padding(10.dp))
    }

}