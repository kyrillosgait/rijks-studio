package com.kyrillosg.rijksstudio.core.data

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface CollectionRepository {

    suspend fun getCollectionItems(): List<CollectionItem>

    suspend fun getDetailedCollectionItem(id: CollectionItem.Id): DetailedCollectionItem?

    fun getCollectionItemsPaginated(): Flow<PagingData<CollectionItem>>
}