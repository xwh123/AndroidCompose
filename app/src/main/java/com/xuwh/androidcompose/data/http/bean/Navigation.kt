package com.xuwh.androidcompose.data.http.bean

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.net.bean
 * @ClassName:      Navigation
 * @Description:    类作用描述
 * @Author:         xuwh
 * @CreateDate:     2025/3/20 下午5:13
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/3/20 下午5:13
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
data class Navigation(var articles: List<Article>,
                      var cid: Int,
                      var name: String)
