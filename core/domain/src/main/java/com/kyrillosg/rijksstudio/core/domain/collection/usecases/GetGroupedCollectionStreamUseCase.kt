package com.kyrillosg.rijksstudio.core.domain.collection.usecases

import com.kyrillosg.rijksstudio.core.domain.collection.CollectionRepository
import com.kyrillosg.rijksstudio.core.domain.collection.model.GroupedCollection
import com.kyrillosg.rijksstudio.core.domain.common.SynchronousUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class GetGroupedCollectionStreamUseCase(
    private val repository: CollectionRepository,
) : SynchronousUseCase<GroupField, Flow<List<GroupedCollection>>> {

    override fun invoke(input: GroupField): Flow<List<GroupedCollection>> {
        return repository.getCollectionItemsStream(input).map { collectionItems ->
            when (input) {
                GroupField.NONE -> {
                    listOf(
                        GroupedCollection(
                            label = null,
                            items = collectionItems,
                        ),
                    )
                }
                GroupField.ARTIST_ASCENDING,
                GroupField.ARTIST_DESCENDING,
                -> {
                    val groupedItems = collectionItems.groupBy { it.author }

                    groupedItems.entries.map {
                        GroupedCollection(
                            label = it.key,
                            items = it.value,
                        )
                    }
                }
            }
        }.flowOn(Dispatchers.Default)
    }
}

enum class GroupField {
    NONE, ARTIST_ASCENDING, ARTIST_DESCENDING
}
