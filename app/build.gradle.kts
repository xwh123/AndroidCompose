plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.gradle.down.task)
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.xuwh.androidcompose"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.xuwh.androidcompose"
        minSdk = 25
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

// 修复后的 build.gradle.kts
val assetDir = File(projectDir, "src/main/assets").absolutePath
val testAssetDir = File(projectDir, "src/androidTest/assets").absolutePath

extra.apply {
    set("ASSET_DIR", assetDir)
    set("TEST_ASSETS_DIR", testAssetDir)
}

apply(from = "download_models.gradle")


dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.compose.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //lifecycle-viewmodel-compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    //paging 分页
    implementation(libs.paging.compose)
    //navigation 导航
    implementation(libs.navigation.compose)
    //hilt 依赖注入
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    //hilt 导航
    implementation(libs.hilt.navigation)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.moshi)
    implementation(libs.cookie)
    // 日志
    implementation(libs.logging.interceptor)

    // Moshi
    implementation(libs.moshi.kotlin)
    kapt(libs.moshi.kotlin.codegen)

    //datastore  数据存储
    implementation(libs.datastore)
    implementation(libs.datastore.core)

    //图片库
    implementation(libs.coil)

    //刷新库
    implementation(libs.swiperefresh)

    //动画
    implementation(libs.compose.animation)

    //状态栏
    implementation(libs.systemuicontroller)

    //livedata
    implementation(libs.runtime.livedata)
    //foundation
    implementation(libs.compose.foundation)

    //分页库
    implementation(libs.accompanist.pager)

    //room
    implementation(libs.room.runtime)
    kapt(libs.room.compiler)

    //权限
    implementation(libs.accompanist.permissions)

    //tensorflow lite 机器学习
    implementation(libs.tensorflow.lite.task)
    implementation(libs.tensorflow.gpu.delegate)
    implementation(libs.tensorflow.lite.gpu)

    //Media3  视频播放
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.ui)
    implementation(libs.media3.common)
}