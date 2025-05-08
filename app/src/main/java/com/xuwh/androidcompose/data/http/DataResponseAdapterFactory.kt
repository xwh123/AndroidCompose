package com.xuwh.androidcompose.data.http

import android.util.Log
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.xuwh.androidcompose.data.http.bean.DataResponse
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.net
 * @ClassName:      DataResponseAdapterFactory
 * @Description:    类作用描述
 * @Author:         xuwh
 * @CreateDate:     2025/3/19 上午12:56
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/3/19 上午12:56
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
class DataResponseAdapterFactory : JsonAdapter.Factory{

    override fun create(type: Type, annotations: MutableSet<out Annotation>, moshi: Moshi): JsonAdapter<*>? {
        if (type !is ParameterizedType || type.rawType != DataResponse::class.java) {
            return null
        }
        Log.d("DataResponseAdapterFactory", "Creating adapter for $type")

        val dataType = type.actualTypeArguments[0]
        val dataAdapter = moshi.adapter<Any>(dataType)

        return object : JsonAdapter<DataResponse<Any>>() {
            @FromJson
            override fun fromJson(reader: JsonReader): DataResponse<Any>? {

                Log.d("DataResponseAdapterFactory", "Parsing JSON data")
                var code = 0
                var message = ""
                var data: Any? = null

                reader.beginObject()
                while (reader.hasNext()) {
                    when (reader.nextName()) {
                        "errorCode" -> code = reader.nextInt()
                        "errorMsg" -> message = reader.nextString()
                        "data" -> data = if (reader.peek() == JsonReader.Token.NULL) {
                            reader.skipValue()
                            // 这里可以根据实际情况设置默认值
                            Any()
                        } else {
                            dataAdapter.fromJson(reader)
                        }
                    }
                }
                reader.endObject()

                return DataResponse(errorCode = code, errorMsg =  message, data = data)
            }

            @ToJson
            override fun toJson(writer: JsonWriter, value: DataResponse<Any>?) {
                writer.beginObject()
                writer.name("errorCode").value(value?.errorCode ?: 0)
                writer.name("errorMsg").value(value?.errorMsg ?: "")
                writer.name("data")
                dataAdapter.toJson(writer, value?.data)
                writer.endObject()
            }
        }
    }
}