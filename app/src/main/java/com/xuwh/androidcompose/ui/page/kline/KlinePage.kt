package com.xuwh.androidcompose.ui.page.kline

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.xuwh.androidcompose.ui.widget.KLineChart
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import com.xuwh.androidcompose.base.state.UIState
import com.xuwh.androidcompose.data.http.bean.KLineData
import com.xuwh.androidcompose.data.http.bean.TickData
import com.xuwh.androidcompose.ui.widget.TickChart
import java.util.Date
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.ui.page.kline
 * @ClassName:      KlinePage
 * @Description:    类作用描述
 * @Author:         xuwh
 * @CreateDate:     2025/4/8 下午9:47
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/4/8 下午9:47
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@Composable
fun KlinePage(snackBarHostState: SnackbarHostState, viewModel: KlineViewModel = hiltViewModel()) {


//    val klineData by viewModel.klineDatas.observeAsState(emptyList())
//    val uiState by viewModel.uiState.collectAsState()
//    Box(modifier = Modifier.fillMaxSize()) {
//        when (uiState) {
//            is UIState.Success -> {
//                KLineChart(klineData)
//            }
//            else ->Unit
//
//        }
//    }

    // 模拟K线数据
    val kLineData = remember {
        List(100) { i ->
            val basePrice = 100f + Random.nextFloat() * 20f
            val open = basePrice
            val close = basePrice + Random.nextFloat() * 10f - 5f
            KLineData(
                timestamp = System.currentTimeMillis() - (30 - i) * 24 * 60 * 60 * 1000L,
                open = open,
                high = max(open, close) + Random.nextFloat() * 3f,
                low = min(open, close) - Random.nextFloat() * 3f,
                close = close,
                volume = Random.nextFloat() * 10000f
            )
        }
    }

    // 模拟时分数据
    val tickData = remember {
        List(100) { i ->
            TickData(
                timestamp = System.currentTimeMillis() - (100 - i) * 60 * 1000L,
                price = 105f + Random.nextFloat() * 10f - 5f,
                volume = Random.nextFloat() * 1000f
            )
        }
    }

    var selectedData by remember { mutableStateOf<KLineData?>(null) }
    var selectedTick by remember { mutableStateOf<TickData?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "K线图",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        KLineChart(
            data = kLineData,
            modifier = Modifier
                .height(350.dp)
                .fillMaxWidth(),
            onHover = { selectedData = it }
        )

        selectedData?.let { data ->
            Text(
                text = "选中: ${Date(data.timestamp).toLocaleString()} " +
                        "开:${data.open} 高:${data.high} 低:${data.low} 收:${data.close} " +
                        "量:${data.volume?.toInt() ?: 0}",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "时分图",
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        TickChart(
            data = tickData,
            modifier = Modifier
                .height(300.dp)
                .fillMaxWidth(),
            onHover = { selectedTick = it }
        )

        selectedTick?.let { tick ->
            Text(
                text = "选中: ${Date(tick.timestamp).toLocaleString()} " +
                        "价:${tick.price} 量:${tick.volume.toInt()}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }

}