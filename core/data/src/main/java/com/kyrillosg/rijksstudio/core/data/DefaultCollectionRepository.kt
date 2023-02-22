package com.kyrillosg.rijksstudio.core.data

import com.kyrillosg.rijksstudio.core.domain.collection.CollectionRepository
import com.kyrillosg.rijksstudio.core.domain.collection.model.CollectionItem
import com.kyrillosg.rijksstudio.core.domain.collection.model.DetailedCollectionItem
import com.kyrillosg.rijksstudio.core.domain.collection.usecases.GroupField
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

internal class DefaultCollectionRepository(
    private val rijksGateway: RijksGateway,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : CollectionRepository {

    private val _itemsUnsorted = MutableStateFlow<List<CollectionItem>>(emptyList())
    private val _itemsByArtist = MutableStateFlow<List<CollectionItem>>(emptyList())
    private val _itemsByArtistDescending = MutableStateFlow<List<CollectionItem>>(emptyList())

    override fun getCollectionItemsStream(groupBy: GroupField): Flow<List<CollectionItem>> {
        return collectionFlowFor(groupBy)
    }

    private fun collectionFlowFor(groupBy: GroupField): MutableStateFlow<List<CollectionItem>> {
        return when (groupBy) {
            GroupField.NONE -> _itemsUnsorted
            GroupField.ARTIST_ASCENDING -> _itemsByArtist
            GroupField.ARTIST_DESCENDING -> _itemsByArtistDescending
        }
    }

    override suspend fun requestMoreCollectionItems(groupBy: GroupField) {
        withContext(dispatcher) {
            val currentItems = collectionFlowFor(groupBy).value

            val nextPage = currentItems.size.div(PAGE_SIZE)
            val filter = CollectionFilter(
                page = nextPage,
                pageSize = PAGE_SIZE,
                groupBy = groupBy,
            )

            Napier.v { "Requesting collection items with - filter: $filter" }

            val newItems = rijksGateway.getCollection(filter).items

            collectionFlowFor(groupBy).value = currentItems + newItems
        }
    }

    override suspend fun invalidateCollectionItems(groupBy: GroupField) {
        collectionFlowFor(groupBy).value = emptyList()
    }

    override suspend fun getDetailedCollectionItem(
        id: CollectionItem.Id,
    ): DetailedCollectionItem {
        return withContext(dispatcher) {
            val filter = CollectionDetailsFilter(id.value)

            Napier.v { "Requesting collection item with - filter: $filter" }

            rijksGateway.getCollectionDetails(filter)
        }
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}

data class CollectionFilter(
    val page: Int = 0,
    val pageSize: Int = DefaultCollectionRepository.PAGE_SIZE,
    val groupBy: GroupField = GroupField.ARTIST_ASCENDING,
    val language: String = "en",
)

data class CollectionDetailsFilter(
    val id: String,
    val language: String = "en",
)
