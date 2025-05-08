package com.xuwh.androidcompose.ui.page.tensorflow

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.YuvImage
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import androidx.activity.ComponentActivity
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraState
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.xuwh.androidcompose.base.event.CameraEvent
import com.xuwh.androidcompose.base.event.UIEvent
import com.xuwh.androidcompose.base.state.UIState
import com.xuwh.androidcompose.base.viewmodel.BaseViewModel
import com.xuwh.androidcompose.ui.helper.ImageClassifierHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.ByteArrayOutputStream
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject
import kotlin.math.log

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.ui.page.tensorflow
 * @ClassName:      CameraViewModel
 * @Description:    相机图片分类识别ViewModel
 * @Author:         xuwh
 * @CreateDate:     2025/4/28 下午5:50
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/4/28 下午5:50
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@HiltViewModel
class CameraViewModel @Inject constructor(
    private val imageClassifier: ImageClassifierHelper) : BaseViewModel<UIState<*>, UIEvent>(UIState.Idle), ImageClassifierHelper.ClassifierListener {


    // 直接暴露的状态属性
    private val _permissionGranted = mutableStateOf(false)
    val permissionGranted: Boolean get() = _permissionGranted.value

    private val _classifications = mutableStateListOf<Classifications>()
    val classifications: List<Classifications> get() = _classifications

    // CameraX
    private var cameraProvider: ProcessCameraProvider? = null
    private val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()

    // 修改属性名并添加私有支持字段
    private var _previewView: PreviewView? = null
    val previewView: PreviewView? get() = _previewView

    private val analysisExecutor = Executors.newSingleThreadExecutor()

    private lateinit var bitmapBuffer: Bitmap

    // Activity 上下文
    private var activityContext: Context? = null

    init {
        // 设置监听器
        imageClassifier.imageClassifierListener = this
        // 按需修改配置参数
        imageClassifier.threshold = 0.7f
    }

    override fun handleEvent(event: UIEvent) {
        when (event) {
            is CameraEvent.OnPermissionResult -> handlePermissionResult(event.isGranted)
            is CameraEvent.StartCamera -> startCamera()
            is CameraEvent.StopCamera -> stopCamera()
            else -> Unit

        }
    }

    private fun handlePermissionResult(isGranted: Boolean) {
        _permissionGranted.value = isGranted
        if (isGranted) {
            previewView?.let { view ->
                bindCameraUseCases()
            }
        } else {
            viewModelScope.launch {
                sendEvent(UIEvent.ShowSnackbar("权限被拒绝"))
            }
        }
    }

    private fun bindCameraUseCases() {
        val previewView = _previewView ?: throw IllegalStateException("PreviewView not set")

        val lifecycleOwner = previewView.context as ComponentActivity

        cameraProvider?.let { provider ->
            // 1. 创建预览用例
            val preview =
                Preview.Builder().setTargetRotation(previewView.display.rotation).build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

            // 2. 创建图像分析用例
            val imageAnalysis =  ImageAnalysis.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .setTargetRotation(previewView.display.rotation)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                .build()
                // The analyzer can then be assigned to the instance
                .also {
                    it.setAnalyzer(cameraExecutor) { image ->
                        if (!::bitmapBuffer.isInitialized) {
                            // The image rotation and RGB image buffer are initialized only once
                            // the analyzer has started running
                            bitmapBuffer = Bitmap.createBitmap(
                                image.width,
                                image.height,
                                Bitmap.Config.ARGB_8888
                            )
                        }

                        processImage(image)
                    }
                }

            // 解除之前的所有绑定
            provider.unbindAll()

            // 3. 选择摄像头（优先后置摄像头）
            val cameraSelector =
                CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

            try {
                // 绑定用例到生命周期
                val camera = provider.bindToLifecycle(
                    previewView.context as ComponentActivity,  // 需要确保已正确设置lifecycleOwner
                    cameraSelector, preview, imageAnalysis
                )

                // 可选的摄像头参数配置
                camera.cameraControl.enableTorch(false)
                camera.cameraInfo.cameraState.observe(lifecycleOwner) { state ->
                    when (state.type) {
                        CameraState.Type.CLOSED -> Log.d("CameraState", "Camera closed")
                        CameraState.Type.OPEN -> Log.d("CameraState", "Camera opened")
                        CameraState.Type.PENDING_OPEN -> Log.d("CameraState", "Camera pending open")
                        CameraState.Type.CLOSING -> Log.d("CameraState", "Camera closing")
                        CameraState.Type.OPENING -> Log.d("CameraState", "Camera opening")
                    }
                }
            } catch (e: Exception) {
                val errorMsg = "Camera binding failed: ${e.javaClass.simpleName}"
                Log.e("CameraViewModel", errorMsg, e)
                viewModelScope.launch { sendEvent(UIEvent.ShowSnackbar(errorMsg)) }
            }
        }
    }

    private fun processImage(imageProxy: ImageProxy) {

        imageProxy.use {
            bitmapBuffer.copyPixelsFromBuffer(imageProxy.planes[0].buffer)
        }

        imageClassifier.classify(bitmapBuffer,getScreenOrientation())

    }

    private fun getScreenOrientation() : Int {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                (activityContext as? Activity)?.display?.rotation ?: 0
            }
            else -> {
                @Suppress("DEPRECATION")
                (activityContext as? Activity)?.windowManager?.defaultDisplay?.rotation ?: 0
            }
        }
    }


    private fun startCamera() {
        // 可添加摄像头启动时的特殊处理
        val context = _previewView?.context ?: run {
            viewModelScope.launch {
                sendEvent(UIEvent.ShowSnackbar("Preview view not initialized"))
            }
            return
        }

        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            try {
                // 获取CameraProvider实例
                cameraProvider = cameraProviderFuture.get()

                // 绑定生命周期
                bindCameraUseCases()
            } catch (e: Exception) {
                val errorMsg = "Camera initialization failed: ${e.localizedMessage}"
                Log.e("CameraViewModel", errorMsg, e)
                viewModelScope.launch {
                    sendEvent(UIEvent.ShowSnackbar(errorMsg))
                }
            }
        }, ContextCompat.getMainExecutor(context))

    }

    private fun stopCamera() {
        analysisExecutor.shutdown()
        cameraProvider?.unbindAll()
        _classifications.clear()
        imageClassifier.clearImageClassifier()
    }

    fun setupPreviewView(view: PreviewView) {
        if (_previewView != null) {
            // 已初始化则跳过
            return
        }

        _previewView = view
        initCameraIfReady()
    }

    // 其他原有代码保持不变...
    private fun initCameraIfReady() {
        if (_previewView != null && permissionGranted) {
            startCamera()
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopCamera()
    }

    override fun onError(error: String) {
       Log.d("CameraViewModel", "识别Error: $error")
    }

    override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
        // 更新状态列表的方式
        results?.let {
//            _classifications.clear()
            _classifications.addAll(it)
        }
//            ?: run {
//            _classifications.clear()
//        }
    }
}