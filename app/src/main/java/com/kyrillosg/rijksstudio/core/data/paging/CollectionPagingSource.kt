package com.kyrillosg.rijksstudio.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kyrillosg.rijksstudio.core.data.CollectionItem
import com.kyrillosg.rijksstudio.core.data.CollectionService
import com.kyrillosg.rijksstudio.core.data.CollectionService.Companion.PAGE_SIZE

class CollectionPagingSource(
    private val service: CollectionService,
) : PagingSource<Int, CollectionItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CollectionItem> {
        val position = params.key ?: 0

        return try {
            val collection = service.getCollectionItems(position, params.loadSize)
            val nextKey = position + (params.loadSize / PAGE_SIZE)
            LoadResult.Page(
                data = collection,
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