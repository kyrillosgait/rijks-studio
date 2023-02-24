package com.kyrillosg.rijksstudio.core.domain.collection.usecases

import com.kyrillosg.rijksstudio.core.domain.collection.CollectionRepository
import com.kyrillosg.rijksstudio.core.domain.collection.CollectionRepository.Companion.DEFAULT_ITEM_COUNT
import com.kyrillosg.rijksstudio.core.domain.common.UseCase

typealias HasMoreItems = Boolean

class RequestMoreCollectionItemsUseCase(
    private val repository: CollectionRepository,
) : UseCase<RequestMoreCollectionItemsUseCase.Params, HasMoreItems> {

    override suspend fun invoke(input: Params): HasMoreItems {
        if (input.refreshData) {
            repository.invalidateCollectionItems(input.groupBy)
        }

        return repository.requestMoreCollectionItems(input.groupBy, input.count, input.searchTerm)
    }

    data class Params(
        val groupBy: GroupField,
        val refreshData: Boolean,
        val count: Int = DEFAULT_ITEM_COUNT,
        val searchTerm: String? = null,
    )
}
