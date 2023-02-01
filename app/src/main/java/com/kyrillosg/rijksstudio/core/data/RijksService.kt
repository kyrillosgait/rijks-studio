package com.kyrillosg.rijksstudio.core.data

import com.kyrillosg.rijksstudio.core.data.network.CollectionDetailsResponse
import com.kyrillosg.rijksstudio.core.data.network.CollectionResponse

interface RijksService {

    suspend fun getCollection(filter: CollectionFilter): CollectionResponse

    suspend fun getCollectionDetails(
        objectNumber: String,
    ): CollectionDetailsResponse

    data class CollectionFilter(
        val page: Int = 0,
        val pageSize: Int = PAGE_SIZE,
        val language: String = "en",
    )

    companion object {
        const val PAGE_SIZE = 20
    }
}