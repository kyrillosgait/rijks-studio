package com.kyrillosg.rijksstudio.core.domain.collection.fakes

import com.kyrillosg.rijksstudio.core.domain.collection.CollectionRepository
import com.kyrillosg.rijksstudio.core.domain.collection.model.CollectionItem
import com.kyrillosg.rijksstudio.core.domain.collection.model.DetailedCollectionItem
import com.kyrillosg.rijksstudio.core.domain.collection.usecases.GroupField
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class FakeCollectionRepository : CollectionRepository {

    private val collection = mutableListOf<DetailedCollectionItem>()

    override suspend fun getDetailedCollectionItem(id: CollectionItem.Id): DetailedCollectionItem {
        return collection.find { it.itemId == id } ?: error("Item with id: $id not found")
    }

    override fun getCollectionItemsStream(groupBy: GroupField): Flow<List<CollectionItem>> {
        return flowOf(collection)
    }

    override suspend fun requestMoreCollectionItems(groupBy: GroupField, count: Int, searchTerm: String?) {
        (collection.size until collection.size + count).map {
            collection.add(fakeItems[it])
        }
    }

    override suspend fun invalidateCollectionItems(groupBy: GroupField) {
        collection.clear()
    }
}
