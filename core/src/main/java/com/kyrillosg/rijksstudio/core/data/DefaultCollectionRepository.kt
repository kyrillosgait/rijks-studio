package com.kyrillosg.rijksstudio.core.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource.LoadResult.Page.Companion.COUNT_UNDEFINED
import com.kyrillosg.rijksstudio.core.cache.cacheOf
import com.kyrillosg.rijksstudio.core.model.CollectionItem
import com.kyrillosg.rijksstudio.core.model.DetailedCollectionItem
import com.kyrillosg.rijksstudio.core.model.GroupBy
import kotlinx.coroutines.flow.Flow

internal class DefaultCollectionRepository(
    private val rijksGateway: RijksGateway,
) : CollectionRepository {

    private val itemCache = cacheOf<CollectionFilter, PaginatedData<List<CollectionItem>>>()
    private val detailCache = cacheOf<CollectionDetailsFilter, DetailedCollectionItem>()

    override fun getCollectionItemsPaginated(groupBy: GroupBy): Flow<PagingData<CollectionItem>> {
        val pageSize = PAGE_SIZE
        val supportsPlaceholders = groupBy == GroupBy.NONE
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                initialLoadSize = pageSize,
                enablePlaceholders = supportsPlaceholders,
                jumpThreshold = if (supportsPlaceholders) 3 * pageSize else COUNT_UNDEFINED
            ),
            pagingSourceFactory = {
                CollectionPagingSource(rijksGateway, itemCache, pageSize, groupBy)
            }
        ).flow
    }

    override suspend fun getDetailedCollectionItem(
        id: CollectionItem.Id,
    ): DetailedCollectionItem? {
        val filter = CollectionDetailsFilter(id.value)
        return detailCache.get(filter)
            ?: rijksGateway.getCollectionDetails(filter)
                ?.also { detailCache.put(filter, it) }
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}

data class CollectionFilter(
    val page: Int = 0,
    val pageSize: Int = DefaultCollectionRepository.PAGE_SIZE,
    val language: String = "en",
    val groupBy: GroupBy = GroupBy.ARTIST_ASCENDING,
)

data class CollectionDetailsFilter(
    val id: String,
    val language: String = "en",
)