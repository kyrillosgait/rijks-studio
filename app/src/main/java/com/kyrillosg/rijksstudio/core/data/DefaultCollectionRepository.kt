package com.kyrillosg.rijksstudio.core.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kyrillosg.rijksstudio.core.data.RijksService.Companion.PAGE_SIZE
import com.kyrillosg.rijksstudio.core.data.paging.CollectionPagingSource
import kotlinx.coroutines.flow.Flow

class DefaultCollectionRepository(
    private val rijksService: RijksService,
) : CollectionRepository {

    override suspend fun getCollectionItems(): List<CollectionItem> {
        return rijksService.getCollectionItems(page = 0, pageSize = 20).artObjects
    }

    override fun getCollectionItemsPaginated(): Flow<PagingData<CollectionItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = 2 * PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                CollectionPagingSource(rijksService)
            }
        ).flow
    }
}