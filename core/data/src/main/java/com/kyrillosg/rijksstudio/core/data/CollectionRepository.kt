package com.kyrillosg.rijksstudio.core.data

import androidx.paging.PagingData
import com.kyrillosg.rijksstudio.core.data.model.CollectionItem
import com.kyrillosg.rijksstudio.core.data.model.DetailedCollectionItem
import com.kyrillosg.rijksstudio.core.data.model.GroupBy
import kotlinx.coroutines.flow.Flow

interface CollectionRepository {

    suspend fun getDetailedCollectionItem(id: CollectionItem.Id): DetailedCollectionItem?

    fun getCollectionItemsPaginated(groupBy: GroupBy): Flow<PagingData<CollectionItem>>
}
