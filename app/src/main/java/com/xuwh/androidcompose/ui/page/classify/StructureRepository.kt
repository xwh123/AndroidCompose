package com.xuwh.androidcompose.ui.page.classify

import com.xuwh.androidcompose.data.http.ApiService
import javax.inject.Inject

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.ui.page.classify
 * @ClassName:      StructureRepository
 * @Description:    类作用描述
 * @Author:         xuwh
 * @CreateDate:     2025/4/5 下午5:11
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/4/5 下午5:11
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
class StructureRepository @Inject constructor(private  val apiService: ApiService) {

    suspend fun queryStructure() = apiService.queryStructure()
}