package com.kyrillosg.rijksstudio.core.domain.collection

import androidx.paging.PagingData
import com.kyrillosg.rijksstudio.core.domain.collection.model.CollectionItem
import com.kyrillosg.rijksstudio.core.domain.collection.model.DetailedCollectionItem
import com.kyrillosg.rijksstudio.core.domain.collection.model.GroupBy
import kotlinx.coroutines.flow.Flow

interface CollectionRepository {

    suspend fun getDetailedCollectionItem(id: CollectionItem.Id): DetailedCollectionItem?

    fun getCollectionItemsPaginated(groupBy: GroupBy): Flow<PagingData<CollectionItem>>
}
