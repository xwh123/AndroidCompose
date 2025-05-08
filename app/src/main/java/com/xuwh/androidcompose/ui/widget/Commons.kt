package com.xuwh.androidcompose.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.ui.widget
 * @ClassName:      Commons
 * @Description:    通用自定义组件
 * @Author:         xuwh
 * @CreateDate:     2025/4/5 下午10:15
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/4/5 下午10:15
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

@Composable
fun TabItem( index: Int,
             tabTexts: List<String>,
             modifier: Modifier = Modifier,
             contentAlign: Alignment = Alignment.Center,
             contentColor: Color = Color.LightGray,
             onTabSelected: ((index: Int) -> Unit)? = null) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(30.dp)
            .horizontalScroll(state = rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .align(contentAlign)
        ) {
            tabTexts.forEachIndexed { i, tabTitle ->
                Text(
                    text = tabTitle,
                    fontSize = if (index == i) 20.sp else 15.sp,
                    fontWeight = if (index == i) FontWeight.SemiBold else FontWeight.Normal,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = 10.dp)
                        .clickable( interactionSource = remember { MutableInteractionSource() },
                            indication = null) {
                            onTabSelected?.invoke(i)
                        },
                    color = if (index == i) Color.Black else contentColor
                )
            }
        }
    }
}
