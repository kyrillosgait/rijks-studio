package com.kyrillosg.rijksstudio.core.domain.collection

import com.kyrillosg.rijksstudio.core.domain.collection.model.CollectionItem
import com.kyrillosg.rijksstudio.core.domain.collection.model.DetailedCollectionItem
import com.kyrillosg.rijksstudio.core.domain.collection.usecases.GroupField
import kotlinx.coroutines.flow.Flow

interface CollectionRepository {

    suspend fun getDetailedCollectionItem(id: CollectionItem.Id): DetailedCollectionItem

    fun getCollectionItemsStream(groupBy: GroupField): Flow<List<CollectionItem>>

    suspend fun requestMoreCollectionItems(groupBy: GroupField)

    suspend fun invalidateCollectionItems(groupBy: GroupField)
}
