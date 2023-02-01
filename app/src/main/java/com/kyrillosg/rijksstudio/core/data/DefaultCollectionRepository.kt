package com.kyrillosg.rijksstudio.core.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kyrillosg.rijksstudio.core.data.cache.simpleCacheOf
import com.kyrillosg.rijksstudio.core.data.model.CollectionItem
import com.kyrillosg.rijksstudio.core.data.model.DetailedCollectionItem
import com.kyrillosg.rijksstudio.core.data.paging.CollectionPagingSource
import kotlinx.coroutines.flow.Flow

class DefaultCollectionRepository(
    private val rijksService: RijksService,
) : CollectionRepository {

    private val itemCache = simpleCacheOf<CollectionFilter, List<CollectionItem>>()
    private val detailCache = simpleCacheOf<CollectionDetailsFilter, DetailedCollectionItem>()

    override fun getCollectionItemsPaginated(): Flow<PagingData<CollectionItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = 3 * PAGE_SIZE,
                enablePlaceholders = true,
            ),
            pagingSourceFactory = {
                CollectionPagingSource(rijksService, itemCache)
            }
        ).flow
    }

    override suspend fun getDetailedCollectionItem(
        id: CollectionItem.Id,
    ): DetailedCollectionItem? {
        val filter = CollectionDetailsFilter(id.value)
        return detailCache.get(filter)
            ?: rijksService.getCollectionDetails(filter)?.also {
                detailCache.put(filter, it)
            }
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