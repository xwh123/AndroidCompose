package com.xuwh.androidcompose.ui.page.common

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.ui.page.common
 * @ClassName:      BottomNavView
 * @Description:    底部导航
 * @Author:         xuwh
 * @CreateDate:     2025/4/2 下午10:00
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/4/2 下午10:00
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

data class NavItemPosition(
    val centerX: Float,
    val width: Float
)

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun FloatingBottomNavigation(
    modifier: Modifier = Modifier,
    selectedIndex: Int = 0,
    maxFloatingHeight: Dp = 20.dp,
    dividerColor: Color = MaterialTheme.colorScheme.primary,
    dividerWidth: Dp = 1.dp,
    content: @Composable () -> Unit
) {
    val itemPositions = remember { mutableStateListOf<NavItemPosition>() }
    val animationProgress = remember { Animatable(0f) }
    val density = LocalDensity.current

    LaunchedEffect(selectedIndex) {
        launch {
            animationProgress.animateTo(1f, animationSpec = tween(300))
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .height(58.dp)
            .fillMaxWidth()
    ) {
        // 绘制贝塞尔曲线背景
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(maxFloatingHeight * 2)
        ) {
            if (itemPositions.size >= 3) {
                drawBezierDivider(
                    itemPositions = itemPositions,
                    selectedIndex = selectedIndex,
                    progress = animationProgress.value,
                    dividerColor = dividerColor,
                    dividerWidth = dividerWidth,
                    density = density
                )
            }
        }

        Layout(
            modifier = Modifier.fillMaxWidth(),
            content = { content() }
        ) { measurables, constraints ->
            // 1. 测量所有子项（使用最小宽度约束）
            val looseConstraints = constraints.copy(minWidth = 0)
            val placeables = measurables.map { it.measure(looseConstraints) }

            // 2. 计算均分宽度
            val itemCount = placeables.size
            val itemWidth = constraints.maxWidth / itemCount

            // 3. 记录新位置的临时列表
            val newPositions = mutableListOf<NavItemPosition>()

            layout(constraints.maxWidth, constraints.maxHeight) {
                placeables.forEachIndexed { index, placeable ->
                    // 4. 计算居中位置
                    val xPosition = index * itemWidth + (itemWidth - placeable.width) / 2
                    val yPosition = (constraints.maxHeight - placeable.height) / 2

                    // 5. 放置元素
                    placeable.placeRelative(x = xPosition, y = yPosition)

                    // 6. 记录精确位置（用于贝塞尔曲线）
                    newPositions.add(
                        NavItemPosition(
                            centerX = xPosition + placeable.width / 2f,
                            width = placeable.width.toFloat()
                        )
                    )
                }

                // 7. 同步位置数据
                if (itemPositions.size != newPositions.size) {
                    itemPositions.clear()
                    itemPositions.addAll(newPositions)
                } else {
                    newPositions.forEachIndexed { i, pos ->
                        itemPositions[i] = pos
                    }
                }
            }
        }
    }
}

/**
 * 绘制贝塞尔曲线分割线
 * @receiver DrawScope
 * @param itemPositions List<NavItemPosition>
 * @param selectedIndex Int
 * @param progress Float
 * @param dividerColor Color
 * @param dividerWidth Dp
 * @param density Density
 */
private fun DrawScope.drawBezierDivider(
    itemPositions: List<NavItemPosition>,
    selectedIndex: Int,
    progress: Float,
    dividerColor: Color,
    dividerWidth: Dp,
    density: Density
) {
    val strokeWidth = with(density) { dividerWidth.toPx() }
    val path = Path().apply {
        reset()

        val selectedItem = itemPositions[selectedIndex]
        val curveDepth = with(density) { 20.dp.toPx() } * progress * (1 - 0.2f * progress)
        val dynamicWidth = 50f * (1 + progress * 0.3f)

        // 计算平滑过渡参数
        val smoothFactor = 0.15f // 平滑系数
        val transitionWidth = dynamicWidth * smoothFactor

        // 左侧直线（添加过渡区）
        moveTo(0f, size.height)
        lineTo(selectedItem.centerX - dynamicWidth - transitionWidth, size.height)

        // 左侧过渡曲线（新增）
        quadraticBezierTo(
            x1 = selectedItem.centerX - dynamicWidth,
            y1 = size.height,
            x2 = selectedItem.centerX - dynamicWidth + transitionWidth,
            y2 = size.height + curveDepth * 0.2f
        )

        // 主凹陷曲线（调整为三次贝塞尔曲线）
        cubicTo(
            x1 = selectedItem.centerX - dynamicWidth * 0.4f,
            y1 = size.height + curveDepth * 0.8f,
            x2 = selectedItem.centerX - dynamicWidth * 0.2f,
            y2 = size.height + curveDepth,
            x3 = selectedItem.centerX,
            y3 = size.height + curveDepth
        )

        // 右侧凹陷曲线（对称调整）
        cubicTo(
            x1 = selectedItem.centerX + dynamicWidth * 0.2f,
            y1 = size.height + curveDepth,
            x2 = selectedItem.centerX + dynamicWidth * 0.4f,
            y2 = size.height + curveDepth * 0.8f,
            x3 = selectedItem.centerX + dynamicWidth - transitionWidth,
            y3 = size.height + curveDepth * 0.2f
        )

        // 右侧过渡曲线（新增）
        quadraticBezierTo(
            x1 = selectedItem.centerX + dynamicWidth,
            y1 = size.height,
            x2 = selectedItem.centerX + dynamicWidth + transitionWidth,
            y2 = size.height
        )

        // 右侧直线
        lineTo(size.width, size.height)
    }

    drawPath(
        path = path,
        color = dividerColor,
        style = Stroke(width = strokeWidth)
    )
}


@Composable
fun FloatingNavItem(
    onClick: () -> Unit,
    icon: @Composable () -> Unit,  // 必须包含的icon参数
    modifier: Modifier = Modifier,
    title: String
) {

    Column(
        modifier = modifier
            .wrapContentHeight()
            .clickable(
                onClick = onClick,
                // 关键修改：禁用点击效果
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ),
        // 垂直居中
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 图标容器（带自动缩放）
        Box(
            modifier = Modifier
                .size(24.dp)
                .padding(top = 0.dp),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }
        Text(
            text = title,
            modifier = Modifier.padding(top = 2.dp),
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}



