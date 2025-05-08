package com.xuwh.androidcompose.paging

import androidx.paging.PagingConfig

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.paging
 * @ClassName:      PagingConfig
 * @Description:    分页配置
 * @Author:         xuwh
 * @CreateDate:     2025/3/30 下午3:51
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/3/30 下午3:51
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
data class MyPagingConfig(val pageSize: Int = 20,
                          val initialLoadSize: Int = 1,
                          val prefetchDistance:Int = 5,
                          val maxSize:Int = PagingConfig.MAX_SIZE_UNBOUNDED,
                          val enablePlaceholders:Boolean = false)
