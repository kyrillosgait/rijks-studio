package com.kyrillosg.rijksstudio.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kyrillosg.rijksstudio.core.data.CollectionFilter
import com.kyrillosg.rijksstudio.core.data.RijksGateway
import com.kyrillosg.rijksstudio.core.data.cache.Cache
import com.kyrillosg.rijksstudio.core.domain.collection.model.CollectionItem
import com.kyrillosg.rijksstudio.core.domain.collection.model.GroupBy

internal class CollectionPagingSource(
    private val service: RijksGateway,
    private val cache: Cache<CollectionFilter, PaginatedData<List<CollectionItem>>>,
    private val pageSize: Int,
    private val groupBy: GroupBy,
) : PagingSource<Int, CollectionItem>() {

    override val jumpingSupported: Boolean = true

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CollectionItem> {
        val page = params.key ?: 0

        return try {
            val filter = CollectionFilter(
                page = page,
                pageSize = params.loadSize,
                groupBy = groupBy,
            )

            val paginatedData = cache.get(filter)
                ?: service.getCollection(filter).also { cache.put(filter, it) }

            val total = paginatedData.total
            val itemsBefore = page * pageSize
            val itemsAfter = total - (itemsBefore + paginatedData.items.size)

            val willReachApiLimit = (page + 1) * params.loadSize >= 10_000

            LoadResult.Page(
                data = paginatedData.items,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (itemsAfter == 0 || willReachApiLimit) null else page + (filter.pageSize / pageSize),
                itemsBefore = itemsBefore,
                itemsAfter = itemsAfter,
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CollectionItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            anchorPosition / pageSize
        }
    }
}
