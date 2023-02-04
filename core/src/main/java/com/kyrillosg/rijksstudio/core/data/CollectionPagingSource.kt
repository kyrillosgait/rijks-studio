package com.kyrillosg.rijksstudio.core.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kyrillosg.rijksstudio.core.data.DefaultCollectionRepository.Companion.PAGE_SIZE
import com.kyrillosg.rijksstudio.core.cache.Cache
import com.kyrillosg.rijksstudio.core.model.CollectionItem

internal class CollectionPagingSource(
    private val service: RijksGateway,
    private val cache: Cache<CollectionFilter, PaginatedData<List<CollectionItem>>>,
) : PagingSource<Int, CollectionItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CollectionItem> {
        val page = params.key ?: 0

        return try {
            val filter = CollectionFilter(page, params.loadSize)

            val paginatedData = cache.get(filter)
                ?: service.getCollection(filter).also { cache.put(filter, it) }

            val total = paginatedData.total
            val itemsBefore = page * PAGE_SIZE
            val itemsAfter = total - itemsBefore + paginatedData.items.size

            val willReachApiLimit = (page + 1) * params.loadSize >= 10_000

            LoadResult.Page(
                data = paginatedData.items,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (itemsAfter == 0 || willReachApiLimit) null else page + 1,
                itemsBefore = page * PAGE_SIZE,
                itemsAfter = itemsAfter,
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