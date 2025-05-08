package com.xuwh.androidcompose.ui.widget

import android.annotation.SuppressLint
import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.xuwh.androidcompose.data.http.bean.KLineData
import kotlin.math.abs
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.min

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.ui.widget
 * @ClassName:      KLineChart
 * @Description:    自定义K线图
 * @Author:         xuwh
 * @CreateDate:     2025/4/8 下午9:43
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/4/8 下午9:43
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun KLineChart( data: List<KLineData>,
                modifier: Modifier = Modifier,
                candleWidth: Float = 10f,
                bullColor: Color = Color.Red,
                bearColor: Color = Color.Green,
                gridColor: Color = Color.LightGray.copy(alpha = 0.5f),
                textColor: Color = Color.Black,
                onHover: (KLineData?) -> Unit = {}) {

    // 计算价格范围
    val priceRange = remember(data) {
        val maxPrice = data.maxOfOrNull { it.high } ?: 0f
        val minPrice = data.minOfOrNull { it.low } ?: 0f
        maxPrice * 1.05f to minPrice * 0.95f
    }

    // 计算时间范围
    val timeRange = remember(data) {
        data.first().timestamp to data.last().timestamp
    }

    var hoverIndex by remember { mutableStateOf<Int?>(null) }

    Canvas(modifier = modifier
        .pointerInteropFilter {
            when (it.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                    val x = it.x
                    val index = (x / candleWidth).toInt().coerceIn(data.indices)
                    hoverIndex = index
                    onHover(data.getOrNull(index))
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
        val canvasWidth = size.width
        val canvasHeight = size.height
        val (maxPrice, minPrice) = priceRange
        val priceDiff = maxPrice - minPrice
        val (startTime, endTime) = timeRange
        val timeDiff = endTime - startTime

        // 绘制网格线
        // 水平网格线(价格)
        val horizontalLines = 5
        for (i in 0..horizontalLines) {
            val price = maxPrice - (priceDiff * i / horizontalLines)
            val y = (price - minPrice) / priceDiff * canvasHeight * 0.9f // 90%给价格，10%给时间标签
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

        // 垂直网格线(时间)
        val verticalLines = min(10, data.size) // 最多显示10条时间线
        for (i in 0..verticalLines) {
            val x = canvasWidth * i / verticalLines
            drawLine(
                color = gridColor,
                start = Offset(x, 0f),
                end = Offset(x, canvasHeight * 0.9f), // 只画到价格区域底部
                strokeWidth = 1f
            )

            // 绘制时间标签
            if (i < data.size) {
                val dataIndex = (data.size * i / verticalLines).coerceAtMost(data.size - 1)
                val date = Date(data[dataIndex].timestamp)
                val dateFormat = SimpleDateFormat("MM-dd", Locale.getDefault())
                val timeText = dateFormat.format(date)

                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        timeText,
                        x - 20f, // 稍微向左偏移使文本居中
                        canvasHeight * 0.95f, // 在底部区域显示
                        android.graphics.Paint().apply {
                            color = textColor.toArgb()
                            textSize = 24f
                        }
                    )
                }
            }
        }

        // 绘制K线
        data.forEachIndexed { index, stock ->
            val x = index * candleWidth + candleWidth / 2
            val openY = canvasHeight * 0.9f - ((stock.open - minPrice) / priceDiff * canvasHeight * 0.9f)
            val closeY = canvasHeight * 0.9f - ((stock.close - minPrice) / priceDiff * canvasHeight * 0.9f)
            val highY = canvasHeight * 0.9f - ((stock.high - minPrice) / priceDiff * canvasHeight * 0.9f)
            val lowY = canvasHeight * 0.9f - ((stock.low - minPrice) / priceDiff * canvasHeight * 0.9f)

            val isBull = stock.close >= stock.open
            var color = if (isBull) bullColor else bearColor

            // 绘制上下影线
            drawLine(
                color = color,
                start = Offset(x, highY),
                end = Offset(x, lowY),
                strokeWidth = 1f
            )

            // 绘制实体
            val top = if (isBull) closeY else openY
            val bottom = if (isBull) openY else closeY
            drawRect(
                color = color,
                topLeft = Offset(x - candleWidth / 2, top),
                size = Size(candleWidth, bottom - top)
            )

            // 高亮显示悬停的K线
            if (index == hoverIndex) {
                drawRect(
                    color = color.copy(alpha = 0.3f),
                    topLeft = Offset(x - candleWidth, 0f),
                    size = Size(candleWidth * 2, canvasHeight * 0.9f)
                )

                // 显示更详细的时间信息
                val date = Date(stock.timestamp)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                val timeText = dateFormat.format(date)

                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        timeText,
                        x - 50f,
                        canvasHeight * 0.98f,
                        android.graphics.Paint().apply {
                            color = Color.Red
                            textSize = 24f
                        }
                    )
                }
            }
        }
    }
}


