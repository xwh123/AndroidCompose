package com.xuwh.androidcompose.ui.page.common

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.xuwh.androidcompose.ui.page.book.BookReaderPage
import com.xuwh.androidcompose.ui.page.book.BookShelfPage
import com.xuwh.androidcompose.ui.page.classify.ClassifyPage
import com.xuwh.androidcompose.ui.page.classify.StructurePage
import com.xuwh.androidcompose.ui.page.example.ExamplePage
import com.xuwh.androidcompose.ui.page.home.HomePage
import com.xuwh.androidcompose.ui.page.kline.KlinePage
import com.xuwh.androidcompose.ui.page.login.LoginPage
import com.xuwh.androidcompose.ui.page.register.RegisterPage
import com.xuwh.androidcompose.ui.page.tensorflow.CameraPage
import com.xuwh.androidcompose.ui.page.video.VideoPage
import com.xuwh.androidcompose.ui.widget.CenterSnackbar
import com.xuwh.androidcompose.ui.widget.CenterSnackbarHost

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.ui.common
 * @ClassName:      AppNavigation
 * @Description:    导航管理
 * @Author:         xuwh
 * @CreateDate:     2025/3/22 下午3:40
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/3/22 下午3:40
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val snackBarHostState = remember { SnackbarHostState() }

    // 保存当前选中的Tab索引
    var selectedIndex by remember { mutableIntStateOf(0) }
    // 添加路由变化监听（在AppNavigation顶部）
    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            selectedIndex = when (destination.route) {
                NavigationManager.HOME -> 0
                NavigationManager.CLASSIFY -> 1
                NavigationManager.KLINE -> 2
                NavigationManager.BOOKSHELF -> 3
                NavigationManager.EXAMPLE -> 4
                else -> selectedIndex
            }
            println("导航至 ${destination.route}, 设置索引为 $selectedIndex")
        }

    }

    Scaffold(modifier = Modifier
        .fillMaxSize(),
//        .navigationBarsPadding(),
        bottomBar = {
            if (currentDestination?.route in listOf(
                    NavigationManager.HOME,
                    NavigationManager.CLASSIFY,
                    NavigationManager.KLINE,
                    NavigationManager.BOOKSHELF,
                    NavigationManager.EXAMPLE
                )
            ) {
                FloatingBottomNavigation(
                    selectedIndex = selectedIndex,
                    maxFloatingHeight = 20.dp,
                    dividerColor = MaterialTheme.colorScheme.primary
                ) {
                    // 首页按钮
                    FloatingNavItem(
                        onClick = {
                            navController.navigate(NavigationManager.HOME) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }

                            selectedIndex = 0
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "首页",
                                tint = if (selectedIndex == 0) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        },
                        title = "首页"
                    )

                    // 分类按钮
                    FloatingNavItem(
                        onClick = {
                            navController.navigate(NavigationManager.CLASSIFY) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                            selectedIndex = 1
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "分类",
                                tint = if (selectedIndex == 1) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        },
                        title = "分类"
                    )

                    // K线图按钮
                    FloatingNavItem(
                        onClick = {
                            navController.navigate(NavigationManager.KLINE) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                            selectedIndex = 2
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "K线图",
                                tint = if (selectedIndex == 2) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        },
                        title = "K线图"
                    )

                    // 书架
                    FloatingNavItem(
                        onClick = {
                            navController.navigate(NavigationManager.BOOKSHELF) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                            selectedIndex = 3
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.AccountBox,
                                contentDescription = "书架",
                                tint = if (selectedIndex == 3) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        },
                        title = "书架"
                    )
                    // 示例
                    FloatingNavItem(
                        onClick = {
                            navController.navigate(NavigationManager.EXAMPLE) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                            selectedIndex = 4
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "示例",
                                tint = if (selectedIndex == 4) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        },
                        title = "示例"
                    )
                }
            }

        }, content = { innerPadding ->
            NavHost(
                modifier = Modifier
                    .padding(innerPadding),
                navController = navController,
                startDestination = NavigationManager.getStartDestination()
            ) {

                composable(route = NavigationManager.LOGIN) {
                    LoginPage(navController, snackBarHostState)
                }

                composable(route = NavigationManager.REGISTER) {
                    RegisterPage(navController, snackBarHostState)
                }

                composable(route = NavigationManager.HOME) {
                    HomePage(navController, snackBarHostState)
                }
                composable(route = NavigationManager.CLASSIFY) {
                    ClassifyPage(navController, snackBarHostState)
                }
                composable(route = NavigationManager.STRUCTURE) {
                    StructurePage(snackBarHostState)
                }

                composable(route = NavigationManager.KLINE) {
                    KlinePage(snackBarHostState)
                }

                composable(route = NavigationManager.BOOKSHELF) {
                    BookShelfPage(navController)
                }

                composable(
                    route = NavigationManager.BOOKREADER,
                    arguments = listOf(
                        navArgument("bookName") {
                            type = NavType.StringType
                            // 添加安全解析
                            nullable = true
                        }
                    )
                ) { entry ->

                    val bookName = entry.arguments?.getString("bookName").orEmpty()

                    BookReaderPage(
                        bookName = bookName,  // 直接传递Uri对象
                        nav = navController
                    )
                }
                composable(route = NavigationManager.EXAMPLE) {
                    ExamplePage(navController)
                }
                composable(route = NavigationManager.CAMERA) {
                    CameraPage()
                }

                composable(route = NavigationManager.VIDEO) {
                    VideoPage()
                }

            }
        }, snackbarHost = {
            CenterSnackbarHost(hostState = snackBarHostState) { data ->
                println("data = $data")
                CenterSnackbar(data = data)
            }
        })
}