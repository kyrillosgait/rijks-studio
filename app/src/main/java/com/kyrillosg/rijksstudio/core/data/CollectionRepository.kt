package com.kyrillosg.rijksstudio.core.data

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface CollectionRepository {

    suspend fun getCollectionItems(): List<CollectionItem>

    fun getCollectionItemsPaginated(): Flow<PagingData<CollectionItem>>
}