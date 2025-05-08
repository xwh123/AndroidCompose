package com.xuwh.androidcompose.ui.page.register

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.xuwh.androidcompose.base.event.RegisterEvent
import com.xuwh.androidcompose.base.event.UIEvent
import com.xuwh.androidcompose.ui.page.common.LabeledTextField
import com.xuwh.androidcompose.ui.page.common.NavigationManager
import com.xuwh.androidcompose.ui.page.common.StateHandler
import com.xuwh.androidcompose.ui.page.common.back
import com.xuwh.androidcompose.ui.page.login.LoginViewModel
import com.xuwh.androidcompose.ui.widget.popSnackBar

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.ui.page.register
 * @ClassName:      RegisterPage
 * @Description:    类作用描述
 * @Author:         xuwh
 * @CreateDate:     2025/3/24 下午9:50
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/3/24 下午9:50
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@Composable
fun RegisterPage(
    nav: NavHostController, snackBarHostState: SnackbarHostState, viewModel:
    RegisterViewModel = hiltViewModel()
) {

    val username by viewModel.userName.collectAsState()
    val password by viewModel.password.collectAsState()
    val repassword by viewModel.repassword.collectAsState()

    val uiState by viewModel.uiState.collectAsState()

    val coroutineState = rememberCoroutineScope()

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
        ){
            Column ( modifier = Modifier
                .padding(padding)
                .fillMaxSize()){
                LabeledTextField(
                    label = "账号", value = username, onValueChange = {
                        viewModel.handleEvent(RegisterEvent.UserNameChanged(it))
                    }, placeholder = "请输入用户名", labelModifier =
                    Modifier.padding(start = 16.dp, top = 100.dp)
                )

                Spacer(Modifier.height(30.dp))

                LabeledTextField(label = "密码", value = password, onValueChange = {viewModel
                    .handleEvent(RegisterEvent.PasswordChanged(it))}, placeholder = "请输入密码", labelModifier = Modifier.padding(start = 16.dp)
                )

                Spacer(Modifier.height(30.dp))

                LabeledTextField(label = "确认密码", value = repassword, onValueChange = {viewModel
                    .handleEvent(RegisterEvent.RePasswordChanged(it))}, placeholder = "请再次输入密码", labelModifier = Modifier.padding(start = 16.dp))

                Spacer(Modifier.height(30.dp))

                Text(
                    "注册", modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                        .clickable {
                            viewModel.handleEvent(RegisterEvent.SubmitRegister)
                        }
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(color = Color.Gray)
                        .wrapContentHeight(Alignment.CenterVertically),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(20.dp))
                Text(
                    "去登录",
                    Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .align
                            (Alignment.CenterHorizontally)
                        .clickable {
                            nav.navigate(NavigationManager.LOGIN)
                        },
                    color = Color.Blue, fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )

            }
            StateHandler(state = uiState,  modifier = Modifier.matchParentSize(),onSuccess = {
                // 注册成功，销毁界面
                nav.back()
            })
        }

    }
}