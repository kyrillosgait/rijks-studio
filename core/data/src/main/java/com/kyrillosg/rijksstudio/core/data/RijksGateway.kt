package com.kyrillosg.rijksstudio.core.data

import com.kyrillosg.rijksstudio.core.data.common.PaginatedData
import com.kyrillosg.rijksstudio.core.domain.collection.model.CollectionItem
import com.kyrillosg.rijksstudio.core.domain.collection.model.DetailedCollectionItem

interface RijksGateway {

    suspend fun getCollection(filter: CollectionFilter): PaginatedData<List<CollectionItem>>

    suspend fun getCollectionDetails(filter: CollectionDetailsFilter): DetailedCollectionItem?
}
