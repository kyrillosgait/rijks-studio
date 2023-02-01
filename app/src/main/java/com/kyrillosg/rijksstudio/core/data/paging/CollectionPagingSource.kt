package com.kyrillosg.rijksstudio.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kyrillosg.rijksstudio.core.data.CollectionItemCache
import com.kyrillosg.rijksstudio.core.data.CollectionItem
import com.kyrillosg.rijksstudio.core.data.RijksService
import com.kyrillosg.rijksstudio.core.data.RijksService.Companion.PAGE_SIZE

class CollectionPagingSource(
    private val service: RijksService,
    private val cache: CollectionItemCache,
) : PagingSource<Int, CollectionItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CollectionItem> {
        val position = params.key ?: 0

        return try {
            val filter = RijksService.CollectionFilter(
                page = position,
                pageSize = params.loadSize
            )
            val items = cache.get(filter)
                ?: service.getCollection(filter).artObjects.also {
                    cache.insert(filter, it)
                }

            val nextKey = position + (params.loadSize / PAGE_SIZE)
            LoadResult.Page(
                data = items,
                prevKey = if (position == 0) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CollectionItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}