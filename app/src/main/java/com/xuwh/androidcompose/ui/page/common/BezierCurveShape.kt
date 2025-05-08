package com.xuwh.androidcompose.ui.page.common

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.ui.page.common
 * @ClassName:      BezierCurveShape
 * @Description:    贝塞尔曲线
 * @Author:         xuwh
 * @CreateDate:     2025/4/2 下午10:01
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/4/2 下午10:01
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
class BezierCurveShape( private val currentOffset: Animatable<Float, AnimationVector1D>):Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {

        // 使用传入的 density 参数转换
        val curveHeight = with(density) { 25.dp.toPx() }
        val curveWidth = with(density) { 90.dp.toPx() }


        return Outline.Generic(Path().apply {
            moveTo(0f, size.height)
            lineTo(size.width, size.height)
            lineTo(size.width, curveHeight)

            // 右侧贝塞尔曲线
            cubicTo(
                size.width - curveWidth / 2, curveHeight,
                currentOffset.value + curveWidth / 2, 0f,
                currentOffset.value, 0f
            )

            // 左侧贝塞尔曲线
            cubicTo(
                currentOffset.value - curveWidth / 2, 0f,
                currentOffset.value - curveWidth, curveHeight,
                currentOffset.value - curveWidth, curveHeight
            )

            lineTo(0f, curveHeight)
            close()
        })
    }
}