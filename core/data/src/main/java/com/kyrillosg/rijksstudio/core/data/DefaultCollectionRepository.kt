package com.kyrillosg.rijksstudio.core.data

import com.kyrillosg.rijksstudio.core.domain.collection.CollectionRepository
import com.kyrillosg.rijksstudio.core.domain.collection.model.CollectionItem
import com.kyrillosg.rijksstudio.core.domain.collection.model.DetailedCollectionItem
import com.kyrillosg.rijksstudio.core.domain.collection.model.GroupBy
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

internal class DefaultCollectionRepository(
    private val rijksGateway: RijksGateway,
) : CollectionRepository {

    private val _itemsUnsorted = MutableStateFlow<List<CollectionItem>>(emptyList())
    private val _itemsByArtist = MutableStateFlow<List<CollectionItem>>(emptyList())
    private val _itemsByArtistDescending = MutableStateFlow<List<CollectionItem>>(emptyList())

    override fun getCollectionItemsStream(groupBy: GroupBy): Flow<List<CollectionItem>> {
        return when (groupBy) {
            GroupBy.NONE -> _itemsUnsorted
            GroupBy.ARTIST_ASCENDING -> _itemsByArtist
            GroupBy.ARTIST_DESCENDING -> _itemsByArtistDescending
        }
    }

    override suspend fun requestMoreCollectionItems(groupBy: GroupBy) {
        withContext(Dispatchers.IO) {
            val currentItems = when (groupBy) {
                GroupBy.NONE -> _itemsUnsorted.value
                GroupBy.ARTIST_ASCENDING -> _itemsByArtist.value
                GroupBy.ARTIST_DESCENDING -> _itemsByArtistDescending.value
            }

            val filter = CollectionFilter(
                page = currentItems.size.div(PAGE_SIZE),
                pageSize = PAGE_SIZE,
                groupBy = groupBy,
            )

            Napier.v { "Requesting collection items with - filter: $filter" }

            val newItems = currentItems + rijksGateway.getCollection(filter).items

            when (groupBy) {
                GroupBy.NONE -> _itemsUnsorted.value = newItems
                GroupBy.ARTIST_ASCENDING -> _itemsByArtist.value = newItems
                GroupBy.ARTIST_DESCENDING -> _itemsByArtistDescending.value = newItems
            }
        }
    }

    override suspend fun getDetailedCollectionItem(
        id: CollectionItem.Id,
    ): DetailedCollectionItem {
        val filter = CollectionDetailsFilter(id.value)

        Napier.v { "Requesting collection item with - filter: $filter" }

        return rijksGateway.getCollectionDetails(filter)
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}

data class CollectionFilter(
    val page: Int = 0,
    val pageSize: Int = DefaultCollectionRepository.PAGE_SIZE,
    val language: String = "en",
    val groupBy: GroupBy = GroupBy.ARTIST_ASCENDING,
)

data class CollectionDetailsFilter(
    val id: String,
    val language: String = "en",
)
