package com.kyrillosg.rijksstudio.core.data

import com.kyrillosg.rijksstudio.core.model.CollectionItem
import com.kyrillosg.rijksstudio.core.model.DetailedCollectionItem

interface RijksService {

    suspend fun getCollection(filter: CollectionFilter): List<CollectionItem>

    suspend fun getCollectionDetails(filter: CollectionDetailsFilter): DetailedCollectionItem?
}
