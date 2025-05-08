package com.xuwh.androidcompose.ui.page.classify

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.xuwh.androidcompose.base.event.ClassifyEvent
import com.xuwh.androidcompose.ui.widget.TabItem

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.ui.page.classify
 * @ClassName:      ClassifyPage
 * @Description:    分类
 * @Author:         xuwh
 * @CreateDate:     2025/4/5 下午3:50
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/4/5 下午3:50
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@Composable
fun ClassifyPage(
    nav: NavHostController, snackBarHostState: SnackbarHostState, viewModel:
    ClassifyViewModel = hiltViewModel()
) {

    val classifyTab = viewModel.classifyTab.value

    val pagerState = rememberPagerState(pageCount = { classifyTab.tabList.size })

    // 同步pager状态和ViewModel状态
    LaunchedEffect(pagerState.currentPage) {

        viewModel.handleEvent(ClassifyEvent.PageSwiped(pagerState.currentPage))

    }

    Scaffold(contentWindowInsets = WindowInsets(0, 0, 0, 0))  { padding ->
        Column {

            Row(modifier = Modifier
                .fillMaxWidth()
//                .background(Color.White)
                .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center ) {

                TabItem(index = pagerState.currentPage, tabTexts = classifyTab.tabList, modifier =
                Modifier.weight(1f), contentAlign = Alignment.CenterStart, onTabSelected = {index->
                    viewModel.handleEvent(ClassifyEvent.SelectTab(index))
                })
            }

            HorizontalPager(state = pagerState) { page ->
                when (page) {
                    0 -> {
                        StructurePage(snackBarHostState)
                    }

                    1 -> {
                        StructurePage(snackBarHostState)
                    }

                    else -> Unit
                }
            }
        }
    }



    // 处理ViewModel状态变化同步到Pager
    LaunchedEffect(classifyTab.selectedTabIndex) {
        pagerState.scrollToPage(classifyTab.selectedTabIndex)
    }

}