package com.xuwh.androidcompose.ui.page.tensorflow

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.xuwh.androidcompose.base.event.CameraEvent
import org.tensorflow.lite.support.label.Category
import org.tensorflow.lite.task.vision.classifier.Classifications

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.ui.page.tensorflow
 * @ClassName:      CameraPage
 * @Description:    相机TensorFlow识别界面
 * @Author:         xuwh
 * @CreateDate:     2025/4/26 下午3:42
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/4/26 下午3:42
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPage(viewModel: CameraViewModel = hiltViewModel()) {

    val lifecycleOwner = LocalLifecycleOwner.current
    // 权限处理
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)


    // 处理权限结果
    LaunchedEffect(cameraPermissionState.status) {
        viewModel.handleEvent(CameraEvent.OnPermissionResult(cameraPermissionState.status.isGranted))
    }

    // 页面内容
    Box(modifier = Modifier.fillMaxSize()) {
        when {
            viewModel.permissionGranted -> MainContent(viewModel)
            else -> PermissionPrompt(viewModel)
        }
    }

    // 生命周期管理
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> viewModel.handleEvent(CameraEvent.StartCamera)
                Lifecycle.Event.ON_STOP -> viewModel.handleEvent(CameraEvent.StopCamera)
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@Composable
private fun MainContent(viewModel: CameraViewModel) {
    Column(modifier = Modifier.fillMaxSize()) {
        // 摄像头预览
        AndroidView(
            factory = { context ->
                PreviewView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                }.also { view ->
                    viewModel.setupPreviewView(view)
                }
            },
            modifier = Modifier
                .weight(1f)
                .aspectRatio(3f / 4f)
        )

        // 分类结果列表
        ClassificationList(viewModel.classifications)
    }
}

@Composable
private fun ClassificationList(classifications: List<Classifications>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        items(classifications.size) { item ->
            classifications[item].categories.firstOrNull()?.let {
                ClassificationItem(it)
            }

        }
    }
}

@Composable
private fun ClassificationItem(item: Category) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = item.label?:"未识别")
        Text(text = if (item.score != null) String.format("%.2f", item.score) else "--")
    }
}

@Composable
private fun ErrorView(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red.copy(alpha = 0.3f)),
        contentAlignment = Alignment.Center
    ) {
        Text(text = message, color = Color.Red)
    }
}

@Composable
private fun PermissionPrompt( viewModel: CameraViewModel) {

    val context = LocalContext.current
    val activity = context as Activity

    // 创建权限请求启动器
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.handleEvent(CameraEvent.OnPermissionResult(isGranted))

        // 处理永久拒绝的情况
        if (!isGranted && !ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.CAMERA
            )) {
            showPermanentlyDeniedDialog(context)
        }
    }

    // 判断是否需要显示解释说明
    val showRationale = remember {
        ActivityCompat.shouldShowRequestPermissionRationale(
            activity,
            Manifest.permission.CAMERA
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Filled.AddCircle,
            contentDescription = "Camera",
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "需要摄像头权限",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "此功能需要访问相机以实现图像识别功能",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        if (showRationale) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "请点击下方按钮授予权限",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                // 触发权限请求
                permissionLauncher.launch(Manifest.permission.CAMERA)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Icon(
                imageVector = Icons.Filled.AddCircle,
                contentDescription = null,
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("授予权限")
        }

        // 显示永久拒绝时的额外操作按钮
        if (!showRationale && !viewModel.permissionGranted) {
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(
                onClick = { openAppSettings(context) }
            ) {
                Text("前往设置")
            }
        }
    }
}

private fun showPermanentlyDeniedDialog(context: Context) {
    AlertDialog.Builder(context)
        .setTitle("权限被永久拒绝")
        .setMessage("您已永久拒绝相机权限，请前往应用设置手动开启")
        .setPositiveButton("去设置") { _, _ ->
            openAppSettings(context)
        }
        .setNegativeButton("取消", null)
        .show()
}

private fun openAppSettings(context: Context) {
    val intent = Intent().apply {
        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        data = Uri.fromParts("package", context.packageName, null)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    context.startActivity(intent)
}