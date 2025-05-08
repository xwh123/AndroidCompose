package com.xuwh.androidcompose.data.http.bean

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.base.net.bean
 * @ClassName:      Article
 * @Description:    文章列表
 * @Author:         xuwh
 * @CreateDate:     2025/3/18 下午5:35
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/3/18 下午5:35
 * @UpdateRemark:   更新说明
 * @Version:        1.0   {
 *         "curPage": 1,
 *         "datas": [ ],
 *         "offset": 0,
 *         "over": false,
 *         "pageCount": 800,
 *         "size": 20,
 *         "total": 15982
 *     }
 */

@Parcelize
data class ArticleResponse(
    val curPage: Int = 0,
    val datas: List<Article>,
    val offset: Int = 0,
    val over: Boolean = false,
    val pageCount: Int = 0,
    val size: Int = 0,
    val total: Int = 0
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class Article(
    val adminAdd: Boolean = false,
    val apkLink: String = "",
    val audit: Int = 0,
    val author: String? = null,
    val canEdit: Boolean = false,
    val chapterId: Int = 0,
    val chapterName: String? = null,
    val collect: Boolean = false,
    val courseId: Int = 0,
    val desc: String? = null,
    val descMd: String? = null,
    val envelopePic: String? = null,
    val fresh: Boolean = false,
    val host: String? = null,
    val id: Int = 0,
    val isAdminAdd: Boolean = false,
    val link: String? = null,
    val niceDate: String? = null,
    val niceShareDate: String? = null,
    val origin: String? = null,
    val prefix: String? = null,
    val projectLink: String? = null,
    val publishTime: Long = 0L,
    val realSuperChapterId: Int = 0,
    val selfVisible: Int = 0,
    val shareDate: Long? = null,
    val shareUser: String? = null,
    val superChapterId: Int = 0,
    val superChapterName: String? = null,
    val tags: List<ArticleTag> = emptyList(),
    val title: String? = null,
    val type: Int = 0,
    val userId: Int = 0,
    val visible: Int = 0,
    val zan: Int = 0,
    // 版本控制  局部更新  修复‌Paging3 无法更新item局部控件
    val version: Long = 0
) : Parcelable{
    // 确保只有 collect 状态变化时不触发全量更新
    override fun equals(other: Any?): Boolean {
        return other is Article &&
                other.id == id &&
                other.collect == collect // 其他字段不参与比较
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + collect.hashCode()
        return result
    }
}

@Parcelize
data class ArticleTag(
    var name: String,
    var url: String
) : Parcelable
