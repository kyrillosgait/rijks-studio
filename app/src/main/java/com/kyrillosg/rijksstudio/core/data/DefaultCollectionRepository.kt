package com.kyrillosg.rijksstudio.core.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kyrillosg.rijksstudio.core.data.RijksService.Companion.PAGE_SIZE
import com.kyrillosg.rijksstudio.core.data.paging.CollectionPagingSource
import kotlinx.coroutines.flow.Flow

class DefaultCollectionRepository(
    private val rijksService: RijksService,
    private val cache: CollectionItemCache,
) : CollectionRepository {

    override suspend fun getCollectionItems(): List<CollectionItem> {
        return rijksService.getCollection(RijksService.CollectionFilter()).artObjects
    }

    override fun getCollectionItemsPaginated(): Flow<PagingData<CollectionItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = 3 * PAGE_SIZE,
                enablePlaceholders = true,
            ),
            pagingSourceFactory = {
                CollectionPagingSource(rijksService, cache)
            }
        ).flow
    }

    override suspend fun getDetailedCollectionItem(id: CollectionItem.Id): DetailedCollectionItem? {
        return rijksService.getCollectionDetails(id.value).artObject
    }
}