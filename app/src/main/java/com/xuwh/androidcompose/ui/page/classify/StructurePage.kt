package com.xuwh.androidcompose.ui.page.classify

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.xuwh.androidcompose.base.event.UIEvent
import com.xuwh.androidcompose.data.http.bean.Children
import com.xuwh.androidcompose.data.http.bean.Structure
import com.xuwh.androidcompose.ui.page.common.StateHandler
import com.xuwh.androidcompose.ui.widget.PagingList
import com.xuwh.androidcompose.ui.widget.popSnackBar

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.ui.page.classify
 * @ClassName:      StructurePage
 * @Description:    类作用描述
 * @Author:         xuwh
 * @CreateDate:     2025/4/5 下午5:10
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/4/5 下午5:10
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StructurePage(
    snackBarHostState: SnackbarHostState, viewModel:
    StructureViewModel = hiltViewModel()
) {
    val structures by viewModel.structure.collectAsState()

    val uiState by viewModel.uiState.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    // 事件处理
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is UIEvent.ShowSnackbar -> popSnackBar(
                    coroutineScope,
                    snackBarHostState,
                    event.message
                )

                else -> Unit
            }
        }
    }


    Scaffold(contentWindowInsets = WindowInsets(0, 0, 0, 0)) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            LazyColumn(
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .fillMaxSize(),
                state = rememberLazyListState(),
                verticalArrangement =  Arrangement.spacedBy(8.dp)
            ) {
                structures.forEach { structure ->


                    stickyHeader {

                        StructureHeader(structure = structure)
                    }
                    structure?.children?.let { children ->
                        item {
                            StructureChildren(children = children)

                        }
                    }
                }
            }
            StateHandler(state = uiState, modifier = Modifier.matchParentSize(), onSuccess = {})
        }
    }
}

@Composable
private fun FlowLayout(  modifier: Modifier = Modifier,
                         horizontalSpacing: Dp = 0.dp,
                         verticalSpacing: Dp = 0.dp,
                         itemHeight: Dp = 40.dp,  // 固定高度参数
                         content: @Composable () -> Unit){
    val density = LocalDensity.current
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        // 单位转换
        val hSpacingPx = with(density) { horizontalSpacing.roundToPx() }
        val vSpacingPx = with(density) { verticalSpacing.roundToPx() }

        val itemHeightPx = with(density) { itemHeight.roundToPx() }


        // 测量所有子项
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints.copy( minHeight = itemHeightPx,
                maxHeight = itemHeightPx,
                minWidth = 0))
        }

        // 布局计算
        val rows = mutableListOf<MutableList<Placeable>>()
        var currentRow = mutableListOf<Placeable>()
        var currentRowWidth = 0

        placeables.forEach { placeable ->
            val totalWidth = currentRowWidth + placeable.width +
                    if (currentRow.isNotEmpty()) hSpacingPx else 0

            if (totalWidth > constraints.maxWidth) {
                rows.add(currentRow)
                currentRow = mutableListOf(placeable)
                currentRowWidth = placeable.width
            } else {
                if (currentRow.isNotEmpty()) currentRowWidth += hSpacingPx
                currentRow.add(placeable)
                currentRowWidth += placeable.width
            }
        }
        if (currentRow.isNotEmpty()) rows.add(currentRow)

        // 计算总高度（确保不会出现负值）
        val totalHeight = if (rows.isEmpty()) {
            0
        } else {
            rows.sumOf { row ->
                row.maxOf { it.height } + vSpacingPx
            } - vSpacingPx
        }.coerceAtLeast(0)  // 确保最小为0

        // 布局
        layout(constraints.maxWidth, totalHeight) {
            var y = 0
            rows.forEach { row ->
                val rowHeight = row.maxOf { it.height }
                var x = 0

                // 计算行内垂直居中位置
                row.forEach { placeable ->
                    val yOffset = (rowHeight - placeable.height) / 2
                    placeable.place(x, y + yOffset)
                    x += placeable.width + hSpacingPx
                }
                y += rowHeight + vSpacingPx
            }
        }
    }
}

@Composable
private fun FlowItem(child: Children) {
    Box(
        modifier = Modifier
            .wrapContentWidth()
            .background(
                color = Color.Blue,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = child.name,
            color = Color.White,
            fontSize = 14.sp,
            softWrap = false ,// 禁止文字换行
            modifier = Modifier.wrapContentWidth()  // 确保文本宽度自适应
            )
    }
}

@Composable
private fun StructureHeader(structure: Structure) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
            .padding(bottom = 10.dp)
    ) {
        Row(
            Modifier.fillMaxWidth().padding(start = 10.dp, top = 10.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(
                modifier = Modifier.width(6.dp),
                color = Color.Black,
                thickness = 18.dp
            )
            Text(
                text = structure.name ?: "compose",
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(start = 10.dp)
            )
        }
    }
}

@Composable
private fun StructureChildren(children: List<Children>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))
            .padding(top = 8.dp)
    ) {
        FlowLayout(
            horizontalSpacing = 8.dp,
            verticalSpacing = 8.dp,
            itemHeight = 40.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .padding(bottom = 10.dp)
        ) {
            children.forEach { child ->
                FlowItem(child = child)
            }
        }
    }
}

