package com.xuwh.androidcompose.data.http

import android.annotation.SuppressLint
import android.content.Context
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.xuwh.androidcompose.data.http.interceptor.LogInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.net
 * @ClassName:      HttpModule
 * @Description:    类作用描述
 * @Author:         xuwh
 * @CreateDate:     2025/3/11 下午8:53
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/3/11 下午8:53
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@Module
@InstallIn(SingletonComponent::class)
object HttpModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(cookieJar: PersistentCookieJar): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            addInterceptor(LogInterceptor())
                .cookieJar(cookieJar)
            sslSocketFactory(createSSLSocketFactory(), TrustAllCerts())
            hostnameVerifier { _, _ -> true }
        }.build()
    }

    @Provides
    @Singleton
    fun provideCookieJar(@ApplicationContext context: Context): PersistentCookieJar {
        return PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context))
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(DataResponseAdapterFactory()) // 添加自定义 Adapter
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://www.wanandroid.com/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    private fun createSSLSocketFactory(): SSLSocketFactory {
        return SSLContext.getInstance("TLS").apply {
            init(null, arrayOf(TrustAllCerts()), SecureRandom())
        }.socketFactory
    }
}

class TrustAllNameVerifier : HostnameVerifier {
    @SuppressLint("BadHostnameVerifier")
    override fun verify(hostname: String?, session: SSLSession?): Boolean = true
}

class TrustAllCerts : X509TrustManager {

    @SuppressLint("TrustAllX509TrustManager")
    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
    }

    @SuppressLint("TrustAllX509TrustManager")
    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
}