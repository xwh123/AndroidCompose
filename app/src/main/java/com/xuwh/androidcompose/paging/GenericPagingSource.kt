package com.xuwh.androidcompose.paging

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.xuwh.androidcompose.data.http.HttpResult
import com.xuwh.androidcompose.data.http.bean.DataResponse
import com.xuwh.androidcompose.data.http.bean.ListWrapper
import kotlinx.coroutines.flow.Flow

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.paging
 * @ClassName:      GenericPagingSource
 * @Description:     通用分页数据源
 * @Author:         xuwh
 * @CreateDate:     2025/3/30 下午4:05
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/3/30 下午4:05
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
fun <T : Any> ViewModel.pagingSource(
    config: MyPagingConfig = MyPagingConfig(),
    callAction: suspend (page: Int) -> DataResponse<ListWrapper<T>>
): Flow<PagingData<T>> {

    return pager(config, 0) {
        val page = it.key ?: 0
        val response = try {
            HttpResult.Success(callAction.invoke(page))
        } catch (e: Exception) {
            HttpResult.Error(e)
        }
        when (response) {
            is HttpResult.Success -> {
                val data = response.result.data
                val hasNotNext = (data!!.datas.size < it.loadSize) && (data.over)
                PagingSource.LoadResult.Page(
                    data = response.result.data!!.datas,
                    prevKey = if (page - 1 > 0) page - 1 else null,
                    nextKey = if (hasNotNext) null else page + 1
                )
            }

            is HttpResult.Error -> {
                Log.d("分页错误：","${response.exception.message}")
                PagingSource.LoadResult.Error(response.exception)
            }
        }

    }
}

fun <K : Any, V : Any> ViewModel.pager(
    config: MyPagingConfig = MyPagingConfig(),
    initialKey: K? = null,
    loadData: suspend (PagingSource.LoadParams<K>) -> PagingSource.LoadResult<K, V>
): Flow<PagingData<V>> {
    val baseConfig = PagingConfig(
        config.pageSize,
        initialLoadSize = config.initialLoadSize,
        prefetchDistance = config.prefetchDistance,
        maxSize = config.maxSize,
        enablePlaceholders = config.enablePlaceholders
    )
    return Pager(
        config = baseConfig,
        initialKey = initialKey
    ) {
        object : PagingSource<K, V>() {
            override suspend fun load(params: LoadParams<K>): LoadResult<K, V> {
                return loadData.invoke(params)
            }

            override fun getRefreshKey(state: PagingState<K, V>): K? {
                return initialKey
            }

        }
    }.flow.cachedIn(viewModelScope)
}