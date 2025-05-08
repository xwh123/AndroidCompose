package com.xuwh.androidcompose.data.store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.preferencesDataStoreFile
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.io.IOException


/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.data.store
 * @ClassName:      DataStoreUtil
 * @Description:    DataStore 工具类   在 Application 中初始化
 * @Author:         xuwh
 * @CreateDate:     2025/3/22 下午3:52
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/3/22 下午3:52
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

// 定义 DataStore 的名称
private const val DATA_STORE_NAME = "compose_store"

// 创建 DataStore 实例
private val Context.dataStore by preferencesDataStore(name = DATA_STORE_NAME)

object DataStoreUtil  {
    private lateinit var context: Context

    fun init(context: Context) {
        this.context = context.applicationContext
    }

    // 保存布尔值
    fun saveBoolean(key: String, value: Boolean) = runBlocking {
        context.dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(key)] = value
        }
    }

    // 获取布尔值
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean = runBlocking {
        val preferences = context.dataStore.data.first()
        return@runBlocking preferences[booleanPreferencesKey(key)] ?: defaultValue
    }

    // 保存整型值
    fun saveInt(key: String, value: Int) = runBlocking {
        context.dataStore.edit { preferences ->
            preferences[intPreferencesKey(key)] = value
        }
    }

    // 获取整型值
    fun getInt(key: String, defaultValue: Int = 0): Int = runBlocking {
        val preferences = context.dataStore.data.first()
        return@runBlocking preferences[intPreferencesKey(key)] ?: defaultValue
    }

    // 保存长整型值
    fun saveLong(key: String, value: Long) = runBlocking {
        context.dataStore.edit { preferences ->
            preferences[longPreferencesKey(key)] = value
        }
    }

    // 获取长整型值
    fun getLong(key: String, defaultValue: Long = 0L): Long = runBlocking {
        val preferences = context.dataStore.data.first()
        return@runBlocking preferences[longPreferencesKey(key)] ?: defaultValue
    }

    // 保存浮点型值
    fun saveFloat(key: String, value: Float) = runBlocking {
        context.dataStore.edit { preferences ->
            preferences[floatPreferencesKey(key)] = value
        }
    }

    // 获取浮点型值
    fun getFloat(key: String, defaultValue: Float = 0f): Float = runBlocking {
        val preferences = context.dataStore.data.first()
        return@runBlocking preferences[floatPreferencesKey(key)] ?: defaultValue
    }

    // 保存字符串值
    fun saveString(key: String, value: String) = runBlocking {
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey(key)] = value
        }
    }

    // 获取字符串值
    fun getString(key: String, defaultValue: String = ""): String = runBlocking {
        val preferences = context.dataStore.data.first()
        return@runBlocking preferences[stringPreferencesKey(key)] ?: defaultValue
    }

    // 清除所有数据
    fun clearAll() = runBlocking {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}