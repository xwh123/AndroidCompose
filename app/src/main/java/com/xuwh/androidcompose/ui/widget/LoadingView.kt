package com.xuwh.androidcompose.ui.widget

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.ui.widget
 * @ClassName:      LoadingView
 * @Description:    自定义加载中view
 * @Author:         xuwh
 * @CreateDate:     2025/3/26 下午4:32
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/3/26 下午4:32
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@Composable
fun LoadingView(
    size: Dp = 80.dp,
    modifier: Modifier = Modifier,
    barCount: Int = 12,
    colors: List<Color> = defaultLoadingColors(),
    animationDuration: Int = 1200
) {
    val density = LocalDensity.current
    val transition = rememberInfiniteTransition()

    // 动画逻辑
    val pos by transition.animateValue(
        initialValue = 0,
        targetValue = barCount - 1,
        typeConverter = Int.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = animationDuration
                for (i in 0 until barCount) {
                    i at (i * animationDuration / barCount)
                }
            }
        )
    )

    // 尺寸计算
    val (rectWidth, rectHeight) = with(density) {
        val totalWidth = size.toPx()
        val width = totalWidth / barCount
        val height = width * 4
        width to height
    }

    Canvas(modifier = modifier.size(size)) {
        val rotationStep = 360f / barCount
        val centerPivot = Offset(size.toPx() / 2, size.toPx() / 2)

        repeat(barCount) { index ->
            rotate(
                degrees = rotationStep * index,
                pivot = centerPivot
            ) {
                drawLoadingBar(
                    index = index,
                    currentPos = pos,
                    colors = colors,
                    rectWidth = rectWidth,
                    rectHeight = rectHeight
                )
            }
        }
    }
}

private fun DrawScope.drawLoadingBar(
    index: Int,
    currentPos: Int,
    colors: List<Color>,
    rectWidth: Float,
    rectHeight: Float
) {
    val color = calculateBarColor(index, currentPos, colors)
    val rect = Rect(
        topLeft = Offset((size.width - rectWidth) / 2, 0f),
        bottomRight = Offset((size.width - rectWidth) / 2 + rectWidth, rectHeight)
    )

    drawRoundRect(
        color = color,
        topLeft = rect.topLeft,
        size = rect.size,
        cornerRadius = CornerRadius(20f)
    )
}

private fun calculateBarColor(
    index: Int,
    currentPos: Int,
    colors: List<Color>
): Color {
    val colorIndexOffset = index - currentPos
    return when {
        colorIndexOffset >= colors.size -> colors.last()
        colorIndexOffset >= 0 -> colors[colorIndexOffset]
        colorIndexOffset >= -colors.size -> colors.last()
        else -> colors.first()
    }
}

private fun defaultLoadingColors() = listOf(
    Color(0xFFbbbbbb),
    Color(0xFFaaaaaa),
    Color(0xFF999999),
    Color(0xFF888888),
    Color(0xFF777777),
    Color(0xFF666666)
)