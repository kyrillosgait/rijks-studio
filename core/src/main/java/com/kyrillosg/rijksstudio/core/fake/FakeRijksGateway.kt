package com.kyrillosg.rijksstudio.core.fake

import com.kyrillosg.rijksstudio.core.data.CollectionDetailsFilter
import com.kyrillosg.rijksstudio.core.data.CollectionFilter
import com.kyrillosg.rijksstudio.core.data.DefaultCollectionRepository.Companion.PAGE_SIZE
import com.kyrillosg.rijksstudio.core.data.PaginatedData
import com.kyrillosg.rijksstudio.core.data.RijksGateway
import com.kyrillosg.rijksstudio.core.model.CollectionItem
import com.kyrillosg.rijksstudio.core.model.DetailedCollectionItem
import kotlinx.coroutines.delay

internal class FakeRijksGateway(
    private val collectionItems: List<DetailedCollectionItem> = (0..2250).map { index ->
        FakeDetailedCollectionItem.create(
            id = CollectionItem.Id(index.toString()),
            title = index.toString(),
            author = when (index) {
                in 0 until 105 -> "Leonardo da Vinci"
                in 105 until 237 -> "Pablo Picasso"
                in 237 until 843 -> "Salvador Dalí"
                in 843 until 2057 -> "Unknown"
                else -> "Vincent van Gogh"
            },
            imageWidth = setOf(144, 192, 256).random(),
            imageHeight = setOf(144, 192, 256).random()
        )
    },
    private val pageSize: Int = PAGE_SIZE,
) : RijksGateway {

    override suspend fun getCollection(filter: CollectionFilter): PaginatedData<List<CollectionItem>> {
        delay(1_000)

        val start = filter.page * pageSize
        val items = (start until start + filter.pageSize).map { collectionItems[it] }.toList()
        return PaginatedData(
            items = items,
            total = collectionItems.count(),
        )
    }

    override suspend fun getCollectionDetails(filter: CollectionDetailsFilter): DetailedCollectionItem? {
        return collectionItems.find { it.itemId.value == filter.id }
    }
}