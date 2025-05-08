package com.xuwh.androidcompose.ui.page.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.xuwh.androidcompose.R
import com.xuwh.androidcompose.base.state.UIState
import com.xuwh.androidcompose.ui.widget.LoadingView

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.ui.common.screen
 * @ClassName:      Components
 * @Description:    通用组件
 * @Author:         xuwh
 * @CreateDate:     2025/3/22 下午1:23
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/3/22 下午1:23
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@Composable
fun <T> StateHandler(
    state: UIState<T>,
    modifier: Modifier = Modifier,
    onLoading: @Composable () -> Unit = { DefaultLoading(modifier) },
    onEmpty: @Composable () -> Unit = { DefaultEmpty(modifier) },
    onSuccess: @Composable (T) -> Unit
) {
    when (state) {
        is UIState.Loading -> onLoading()
        is UIState.Empty -> onEmpty()
        is UIState.Success -> onSuccess(state.data)
        UIState.Idle -> Unit
    }
}

@Composable
fun DefaultLoading(modifier: Modifier = Modifier) {
    Box(
        modifier
            .wrapContentSize()
            .background(Color.LightGray, shape = RoundedCornerShape(16.dp))
            .padding(20.dp), contentAlignment = Alignment.Center
    ) {

        LoadingView(60.dp, modifier = Modifier
            .padding(20.dp)
            .clip(RoundedCornerShape(16.dp)))
    }
}


@Composable
fun DefaultEmpty(modifier: Modifier = Modifier) {
    Box(
        modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                bitmap = ImageBitmap.imageResource(id = R.mipmap.icon_empty_data),
                contentDescription = "Empty",
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(64.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "No data available",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
fun LabeledTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChange:
        (String) -> Unit,
    placeholder: String,
    labelModifier: Modifier = Modifier.padding(start = 16.dp),
    isPassword: Boolean = false
) {
    Column(modifier = modifier) {
        Text(text = label, modifier = labelModifier)
        Spacer(Modifier.height(16.dp))
        TextField(
            value = value, onValueChange = onValueChange, modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Gray),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            placeholder = { Text(text = placeholder, color = Color.LightGray) },
            visualTransformation = if (isPassword) PasswordVisualTransformation() else
                VisualTransformation.None, keyboardOptions = if (isPassword) KeyboardOptions
                (keyboardType = KeyboardType.Password) else KeyboardOptions.Default
        )
    }
}
