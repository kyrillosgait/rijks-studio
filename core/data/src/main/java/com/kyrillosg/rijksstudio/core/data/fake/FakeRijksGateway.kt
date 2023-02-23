package com.kyrillosg.rijksstudio.core.data.fake

import com.kyrillosg.rijksstudio.core.data.CollectionDetailsFilter
import com.kyrillosg.rijksstudio.core.data.CollectionFilter
import com.kyrillosg.rijksstudio.core.data.RijksGateway
import com.kyrillosg.rijksstudio.core.data.common.PaginatedData
import com.kyrillosg.rijksstudio.core.domain.collection.CollectionRepository
import com.kyrillosg.rijksstudio.core.domain.collection.fakes.fakeItems
import com.kyrillosg.rijksstudio.core.domain.collection.model.CollectionItem
import com.kyrillosg.rijksstudio.core.domain.collection.model.DetailedCollectionItem
import kotlinx.coroutines.delay

internal class FakeRijksGateway(
    private val collectionItems: List<DetailedCollectionItem> = fakeItems,
    private val pageSize: Int = CollectionRepository.DEFAULT_ITEM_COUNT,
) : RijksGateway {

    override suspend fun getCollection(filter: CollectionFilter): PaginatedData<List<CollectionItem>> {
        delay(1_000)

        val start = filter.page * pageSize
        val items = (start until start + filter.pageSize).mapNotNull {
            runCatching { collectionItems[it] }.getOrNull()
        }.toList()
        return PaginatedData(
            items = items,
            total = collectionItems.count(),
        )
    }

    override suspend fun getCollectionDetails(filter: CollectionDetailsFilter): DetailedCollectionItem {
        return collectionItems.find { it.itemId.value == filter.id } ?: error("Item with id: ${filter.id} not found")
    }
}
