package com.xuwh.androidcompose.ui.page.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.xuwh.androidcompose.base.event.LoginEvent
import com.xuwh.androidcompose.base.event.UIEvent
import com.xuwh.androidcompose.ui.page.common.LabeledTextField
import com.xuwh.androidcompose.ui.page.common.NavigationManager
import com.xuwh.androidcompose.ui.page.common.StateHandler
import com.xuwh.androidcompose.ui.widget.popSnackBar

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.ui.login.page
 * @ClassName:      LoginPage
 * @Description:    类作用描述
 * @Author:         xuwh
 * @CreateDate:     2025/3/22 下午2:46
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/3/22 下午2:46
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@Composable
fun LoginPage(
    nav: NavHostController, snackBarHostState: SnackbarHostState, viewModel:
    LoginViewModel = hiltViewModel()
) {
    val username by viewModel.userName.collectAsState()
    val password by viewModel.password.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    val coroutineState = rememberCoroutineScope()

    //处理事件
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is UIEvent.ShowSnackbar -> popSnackBar(
                    coroutineState,
                    snackBarHostState,
                    event.message
                )

                else -> Unit
            }
        }
    }

    Scaffold { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // 登录表单
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 用户名输入

                LabeledTextField(label = "用户名", value = username, onValueChange = {viewModel
                    .handleEvent(LoginEvent.UserNameChanged(it))}, placeholder = "请输入用户名",
                    labelModifier = Modifier.padding(start = 16.dp))

                Spacer(Modifier.height(16.dp))

                // 密码输入
                LabeledTextField(label = "密码", value = password, onValueChange = {viewModel
                    .handleEvent(LoginEvent.PasswordChanged(it))}, placeholder = "请输入密码",
                    labelModifier = Modifier.padding(start = 16.dp))

                Spacer(Modifier.height(32.dp))

                // 登录按钮
                Button(
                    onClick = { viewModel.handleEvent(LoginEvent.SubmitLogin) },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("登录", Modifier.padding(8.dp))
                }
                Spacer(Modifier.height(32.dp))

                Text(
                    "去注册",
                    Modifier
                        .padding(8.dp)
                        .clickable {
                            nav.navigate(NavigationManager.REGISTER)
                        },
                    color = Color.Blue, fontSize = 16.sp
                )

            }

            // 状态处理层
            StateHandler(
                state = uiState,
                modifier = Modifier.matchParentSize(), onSuccess = {
                    // 登录成功，跳转到首页
                    nav.navigate(NavigationManager.HOME){
                        // 清空返回栈确保不能返回登录页
                        popUpTo(NavigationManager.LOGIN) { inclusive = true }
                        launchSingleTop = true
                    }
                })
        }
    }
}

