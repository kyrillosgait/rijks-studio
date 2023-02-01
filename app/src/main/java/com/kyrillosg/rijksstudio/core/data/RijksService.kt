package com.kyrillosg.rijksstudio.core.data

import com.kyrillosg.rijksstudio.core.data.model.CollectionItem
import com.kyrillosg.rijksstudio.core.data.model.DetailedCollectionItem

interface RijksService {

    suspend fun getCollection(filter: CollectionFilter): List<CollectionItem>

    suspend fun getCollectionDetails(filter: CollectionDetailsFilter): DetailedCollectionItem?
}
