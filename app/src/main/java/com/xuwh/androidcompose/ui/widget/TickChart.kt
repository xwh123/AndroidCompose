package com.xuwh.androidcompose.ui.widget

import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInteropFilter
import com.xuwh.androidcompose.data.http.bean.TickData
import java.util.Date

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.ui.widget
 * @ClassName:      TickChart
 * @Description:    时分图
 * @Author:         xuwh
 * @CreateDate:     2025/4/9 下午9:22
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/4/9 下午9:22
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TickChart(
    data: List<TickData>,
    modifier: Modifier = Modifier,
    lineColor: Color = Color.Blue,
    volumeColor: Color = Color.Gray.copy(alpha = 0.5f),
    gridColor: Color = Color.LightGray.copy(alpha = 0.5f),
    textColor: Color = Color.Black,
    onHover: (TickData?) -> Unit = {}
) {
    // 计算价格范围
    val priceRange = remember(data) {
        val maxPrice = data.maxOfOrNull { it.price } ?: 0f
        val minPrice = data.minOfOrNull { it.price } ?: 0f
        maxPrice * 1.05f to minPrice * 0.95f
    }

    // 计算成交量范围
    val volumeRange = remember(data) {
        val maxVolume = data.maxOfOrNull { it.volume } ?: 0f
        0f to maxVolume * 1.1f
    }

    // 计算时间范围
    val timeRange = remember(data) {
        data.first().timestamp to data.last().timestamp
    }

    var hoverIndex by remember { mutableStateOf<Int?>(null) }
    var canvasSize by remember { mutableStateOf(Size.Zero) }

    Canvas(modifier = modifier
        .pointerInteropFilter { event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                    val viewWidth = canvasSize.width
                    val x = event.x
                    val index = if (data.isNotEmpty() && viewWidth > 0) {
                        (x / (viewWidth / data.size)).toInt().coerceIn(data.indices)
                    } else {
                        null
                    }
                    hoverIndex = index
                    onHover(index?.let { data.getOrNull(it) })
                    true
                }
                MotionEvent.ACTION_UP -> {
                    hoverIndex = null
                    onHover(null)
                    true
                }
                else -> false
            }
        }
    ) {
        canvasSize = size
        val canvasWidth = size.width
        val canvasHeight = size.height
        val (maxPrice, minPrice) = priceRange
        val priceDiff = maxPrice - minPrice
        val (minVolume, maxVolume) = volumeRange
        val volumeDiff = maxVolume - minVolume
        val (startTime, endTime) = timeRange
        val timeDiff = endTime - startTime

        // 绘制网格线
        // 水平网格线(价格)
        val horizontalLines = 5
        for (i in 0..horizontalLines) {
            val price = maxPrice - (priceDiff * i / horizontalLines)
            val y = (price - minPrice) / priceDiff * canvasHeight * 0.8f // 80%给价格，20%给成交量
            drawLine(
                color = gridColor,
                start = Offset(0f, y),
                end = Offset(canvasWidth, y),
                strokeWidth = 1f
            )
            // 绘制价格标签
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    "%.2f".format(price),
                    10f,
                    y - 5f,
                    android.graphics.Paint().apply {
                        color = textColor.toArgb()
                        textSize = 24f
                    }
                )
            }
        }

        // 绘制价格线
        val path = Path()
        var firstPoint = true
        data.forEachIndexed { index, tick ->
            val x = index * canvasWidth / data.size
            val y = canvasHeight * 0.8f - ((tick.price - minPrice) / priceDiff * canvasHeight * 0.8f)

            if (firstPoint) {
                path.moveTo(x, y)
                firstPoint = false
            } else {
                path.lineTo(x, y)
            }
        }
        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = 2f)
        )

        // 绘制成交量柱状图
        data.forEachIndexed { index, tick ->
            val x = index * canvasWidth / data.size
            val barWidth = canvasWidth / data.size * 0.8f
            val height = (tick.volume / volumeDiff) * canvasHeight * 0.2f

            drawRect(
                color = volumeColor,
                topLeft = Offset(x - barWidth / 2, canvasHeight * 0.8f),
                size = Size(barWidth, height)
            )
        }

        // 高亮显示悬停的点
        hoverIndex?.let { index ->
            if (index in data.indices) {
                val tick = data[index]
                val x = index * canvasWidth / data.size
                val y = canvasHeight * 0.8f - ((tick.price - minPrice) / priceDiff * canvasHeight * 0.8f)

                // 绘制十字线
                drawLine(
                    color = Color.Black.copy(alpha = 0.5f),
                    start = Offset(0f, y),
                    end = Offset(canvasWidth, y),
                    strokeWidth = 1f
                )
                drawLine(
                    color = Color.Black.copy(alpha = 0.5f),
                    start = Offset(x, 0f),
                    end = Offset(x, canvasHeight),
                    strokeWidth = 1f
                )

                // 绘制悬停点的信息
                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        "时间: ${Date(tick.timestamp).toLocaleString()}",
                        x + 10f,
                        y - 30f,
                        android.graphics.Paint().apply {
                            color = textColor.toArgb()
                            textSize = 24f
                        }
                    )
                    drawText(
                        "价格: %.2f".format(tick.price),
                        x + 10f,
                        y - 60f,
                        android.graphics.Paint().apply {
                            color = textColor.toArgb()
                            textSize = 24f
                        }
                    )
                    drawText(
                        "成交量: %.0f".format(tick.volume),
                        x + 10f,
                        y - 90f,
                        android.graphics.Paint().apply {
                            color = textColor.toArgb()
                            textSize = 24f
                        }
                    )
                }
            }
        }
    }
}