package com.kyrillosg.rijksstudio.core.data

import androidx.paging.PagingData
import com.kyrillosg.rijksstudio.core.data.model.CollectionItem
import com.kyrillosg.rijksstudio.core.data.model.DetailedCollectionItem
import com.kyrillosg.rijksstudio.core.data.model.GroupBy
import kotlinx.coroutines.flow.Flow

interface CollectionRepository {

    suspend fun getDetailedCollectionItem(id: com.kyrillosg.rijksstudio.core.data.model.CollectionItem.Id): com.kyrillosg.rijksstudio.core.data.model.DetailedCollectionItem?

    fun getCollectionItemsPaginated(groupBy: com.kyrillosg.rijksstudio.core.data.model.GroupBy): Flow<PagingData<com.kyrillosg.rijksstudio.core.data.model.CollectionItem>>
}