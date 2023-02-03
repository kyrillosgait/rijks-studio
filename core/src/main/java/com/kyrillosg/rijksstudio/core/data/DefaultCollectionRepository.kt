package com.kyrillosg.rijksstudio.core.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kyrillosg.rijksstudio.core.cache.cacheOf
import com.kyrillosg.rijksstudio.core.model.CollectionItem
import com.kyrillosg.rijksstudio.core.model.DetailedCollectionItem
import kotlinx.coroutines.flow.Flow

internal class DefaultCollectionRepository(
    private val rijksGateway: RijksGateway,
) : CollectionRepository {

    private val itemCache = cacheOf<CollectionFilter, List<CollectionItem>>()
    private val detailCache = cacheOf<CollectionDetailsFilter, DetailedCollectionItem>()

    override fun getCollectionItemsPaginated(): Flow<PagingData<CollectionItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = 3 * PAGE_SIZE,
                enablePlaceholders = true,
            ),
            pagingSourceFactory = {
                CollectionPagingSource(rijksGateway, itemCache)
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
)

data class CollectionDetailsFilter(
    val id: String,
    val language: String = "en",
)