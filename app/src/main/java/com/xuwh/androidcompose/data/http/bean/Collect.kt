package com.xuwh.androidcompose.data.http.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.net.bean
 * @ClassName:      Collect
 * @Description:    类作用描述
 * @Author:         xuwh
 * @CreateDate:     2025/3/21 下午2:39
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/3/21 下午2:39
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
data class Collect(
    val curPage: Int,
    val datas: List<CollectArticle>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)
@Parcelize
data class CollectArticle(
    val author: String,
    val chapterId: Int,
    val chapterName: String,
    val courseId: Int,
    val desc: String,
    val envelopePic: String,
    val id: Int,
    val link: String,
    val niceDate: String,
    val origin: String,
    val originId: Int,
    val publishTime: Long,
    val title: String,
    val userId: Int,
    val visible: Int,
    val zan: Int
):Parcelable