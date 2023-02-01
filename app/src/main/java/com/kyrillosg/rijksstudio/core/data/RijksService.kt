package com.kyrillosg.rijksstudio.core.data

import com.kyrillosg.rijksstudio.core.data.network.CollectionResponse

interface RijksService {

    suspend fun getCollectionItems(page: Int = 0, pageSize: Int = PAGE_SIZE): CollectionItemResponse

    companion object {
        const val PAGE_SIZE = 20
    }
}