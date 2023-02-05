package com.kyrillosg.rijksstudio.core.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource.LoadResult.Page.Companion.COUNT_UNDEFINED
import com.kyrillosg.rijksstudio.core.data.model.CollectionItem
import com.kyrillosg.rijksstudio.core.data.paging.CollectionPagingSource
import com.kyrillosg.rijksstudio.core.data.paging.PaginatedData
import kotlinx.coroutines.flow.Flow

internal class DefaultCollectionRepository(
    private val rijksGateway: RijksGateway,
) : CollectionRepository {

    private val itemCache =
        com.kyrillosg.rijksstudio.core.data.cache.cacheOf<CollectionFilter, PaginatedData<List<CollectionItem>>>()
    private val detailCache =
        com.kyrillosg.rijksstudio.core.data.cache.cacheOf<CollectionDetailsFilter, com.kyrillosg.rijksstudio.core.data.model.DetailedCollectionItem>()

    override fun getCollectionItemsPaginated(groupBy: com.kyrillosg.rijksstudio.core.data.model.GroupBy): Flow<PagingData<com.kyrillosg.rijksstudio.core.data.model.CollectionItem>> {
        val pageSize = PAGE_SIZE
        val supportsPlaceholders = groupBy == com.kyrillosg.rijksstudio.core.data.model.GroupBy.NONE
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
        id: com.kyrillosg.rijksstudio.core.data.model.CollectionItem.Id,
    ): com.kyrillosg.rijksstudio.core.data.model.DetailedCollectionItem? {
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
    val groupBy: com.kyrillosg.rijksstudio.core.data.model.GroupBy = com.kyrillosg.rijksstudio.core.data.model.GroupBy.ARTIST_ASCENDING,
)

data class CollectionDetailsFilter(
    val id: String,
    val language: String = "en",
)