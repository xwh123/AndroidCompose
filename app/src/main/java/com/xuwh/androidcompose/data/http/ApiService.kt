package com.xuwh.androidcompose.data.http

import com.xuwh.androidcompose.data.http.bean.Article
import com.xuwh.androidcompose.data.http.bean.ArticleResponse
import com.xuwh.androidcompose.data.http.bean.Banner
import com.xuwh.androidcompose.data.http.bean.Collect
import com.xuwh.androidcompose.data.http.bean.DataResponse
import com.xuwh.androidcompose.data.http.bean.ListWrapper
import com.xuwh.androidcompose.data.http.bean.Navigation
import com.xuwh.androidcompose.data.http.bean.Structure
import com.xuwh.androidcompose.data.http.bean.User
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 *
 * @ProjectName:    BaseKotlin
 * @Package:        com.xuwh.androidcompose.net
 * @ClassName:      ApiService
 * @Description:    接口类
 * @Author:         xuwh
 * @CreateDate:     2025/3/11 下午8:16
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/3/11 下午8:16
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
interface ApiService {

    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(@Field("username") username: String,@Field("password") password: String)
    : DataResponse<User>

    @FormUrlEncoded
    @POST("user/register")
    suspend fun register(@Field("username") username: String,@Field("password") password: String,
                        @Field("repassword") repassword: String): DataResponse<User>

    @GET("banner/json")
    suspend fun getBanner(): DataResponse<List<Banner>>

    @GET("article/list/{page}/json")
    suspend fun getHomeArticle(@Path("page") page:Int): DataResponse<ListWrapper<Article>>

    //收藏站内文章
    @POST("/lg/collect/{id}/json")
    suspend fun collectInnerArticle(@Path("id") id: Int): DataResponse<Any>

    //取消收藏站内文章（在首页等列表里取消）
    @POST("/lg/uncollect_originId/{id}/json")
    suspend fun uncollectInnerArticle(@Path("id") id: Int): DataResponse<Any>

    //体系数据
    @GET("tree/json")
    suspend fun queryStructure(): DataResponse<List<Structure>>


    @GET("navi/json")
    suspend fun queryNavigation(): DataResponse<List<Navigation>>

    @GET("lg/collect/list/{page}/json")
    suspend fun queryCollectArticle(@Path("page") page:Int): DataResponse<Collect>

    @GET("user/logout/json")
    suspend fun logout(): DataResponse<Any>

}