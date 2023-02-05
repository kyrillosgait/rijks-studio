package com.kyrillosg.rijksstudio.core.data

import com.kyrillosg.rijksstudio.core.data.model.CollectionItem
import com.kyrillosg.rijksstudio.core.data.model.DetailedCollectionItem
import com.kyrillosg.rijksstudio.core.data.paging.PaginatedData

interface RijksGateway {

    suspend fun getCollection(filter: CollectionFilter): PaginatedData<List<CollectionItem>>

    suspend fun getCollectionDetails(filter: CollectionDetailsFilter): DetailedCollectionItem?
}
