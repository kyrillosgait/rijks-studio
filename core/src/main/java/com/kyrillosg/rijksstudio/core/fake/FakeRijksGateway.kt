package com.kyrillosg.rijksstudio.core.fake

import com.kyrillosg.rijksstudio.core.data.CollectionDetailsFilter
import com.kyrillosg.rijksstudio.core.data.CollectionFilter
import com.kyrillosg.rijksstudio.core.data.RijksGateway
import com.kyrillosg.rijksstudio.core.model.CollectionItem
import com.kyrillosg.rijksstudio.core.model.DetailedCollectionItem

internal class FakeRijksGateway(
    private val collectionItems: List<DetailedCollectionItem> = (0..250).map {
        FakeDetailedCollectionItem.create(
            author = if (it < 100) "Salvador Dalí" else "Vincent van Gogh",
            imageWidth = setOf(144, 192, 256).random(),
            imageHeight = setOf(144, 192, 256).random()
        )
    }
) : RijksGateway {

    override suspend fun getCollection(filter: CollectionFilter): List<CollectionItem> {
        val start = filter.page * filter.pageSize
        return (start until start + filter.pageSize).map { collectionItems[it] }.toList()
    }

    override suspend fun getCollectionDetails(filter: CollectionDetailsFilter): DetailedCollectionItem? {
        return collectionItems.find { it.itemId.value == filter.id }
    }
}