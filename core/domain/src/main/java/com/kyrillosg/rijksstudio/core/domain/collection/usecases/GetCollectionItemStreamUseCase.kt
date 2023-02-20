package com.kyrillosg.rijksstudio.core.domain.collection.usecases

import com.kyrillosg.rijksstudio.core.domain.collection.CollectionRepository
import com.kyrillosg.rijksstudio.core.domain.collection.model.GroupBy
import com.kyrillosg.rijksstudio.core.domain.collection.model.GroupedCollectionItems
import com.kyrillosg.rijksstudio.core.domain.common.SynchronousUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class GetCollectionItemStreamUseCase(
    private val repository: CollectionRepository,
) : SynchronousUseCase<GroupBy, Flow<List<GroupedCollectionItems>>> {

    override fun invoke(input: GroupBy): Flow<List<GroupedCollectionItems>> {
        return repository.getCollectionItemsStream(input).map { collectionItems ->
            when (input) {
                GroupBy.NONE -> {
                    listOf(
                        GroupedCollectionItems(
                            label = null,
                            items = collectionItems,
                        ),
                    )
                }
                GroupBy.ARTIST_ASCENDING,
                GroupBy.ARTIST_DESCENDING,
                -> {
                    val groupedItems = collectionItems.groupBy { it.author }

                    groupedItems.entries.map {
                        GroupedCollectionItems(
                            label = it.key,
                            items = it.value,
                        )
                    }
                }
            }
        }.flowOn(Dispatchers.Default)
    }
}
