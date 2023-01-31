package com.kyrillosg.rijksstudio.core.data

interface CollectionService {

    suspend fun getCollectionItems(page: Int = 0, pageSize: Int = PAGE_SIZE): List<CollectionItem>

    companion object {
        const val PAGE_SIZE = 20
    }
}