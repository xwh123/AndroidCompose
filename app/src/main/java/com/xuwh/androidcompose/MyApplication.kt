package com.xuwh.androidcompose

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraXConfig
import com.xuwh.androidcompose.data.store.DataStoreUtil
import com.xuwh.androidcompose.room.BookManager
import dagger.hilt.android.HiltAndroidApp

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose
 * @ClassName:      MyApplication
 * @Description:    1. 所有使用 Hilt 的 App 必须包含 一个使用 @HiltAndroidApp 注解的 Application
 *  2. @HiltAndroidApp 将会触发 Hilt 代码的生成，包括用作应用程序依赖项容器的基类
 *  3. 生成的 Hilt 组件依附于 Application 的生命周期，它也是 App 的父组件，提供其他组件访问的依赖
 *  4. 在 Application 中设置好 @HiltAndroidApp 之后，就可以使用 Hilt 提供的组件了，
 *     Hilt 提供的 @AndroidEntryPoint 注解用于提供 Android 类的依赖（Activity、Fragment、View、Service、BroadcastReceiver）等等
 *     Application 使用 @HiltAndroidApp 注解
 * @Author:         xuwh
 * @CreateDate:     2025/3/21 下午7:40
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/3/21 下午7:40
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@HiltAndroidApp
class MyApplication :Application() , CameraXConfig.Provider{

    override fun onCreate() {
        super.onCreate()
        DataStoreUtil.init(context = this)
    }

    override fun getCameraXConfig(): CameraXConfig {
        return CameraXConfig.Builder.fromConfig(Camera2Config.defaultConfig())
            .setSchedulerHandler(Handler(Looper.getMainLooper()))
            .setMinimumLoggingLevel(Log.ERROR)
            .build()
    }
}