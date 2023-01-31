package com.kyrillosg.rijksstudio.core.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kyrillosg.rijksstudio.core.data.CollectionService.Companion.PAGE_SIZE
import com.kyrillosg.rijksstudio.core.data.paging.CollectionPagingSource
import kotlinx.coroutines.flow.Flow

class DefaultCollectionRepository(
    private val collectionService: CollectionService,
) : CollectionRepository {

    override suspend fun getCollectionItems(): List<CollectionItem> {
        return collectionService.getCollectionItems(page = 0, pageSize = 20)
    }

    override fun getCollectionItemsPaginated(): Flow<PagingData<CollectionItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                CollectionPagingSource(collectionService)
            }
        ).flow
    }
}