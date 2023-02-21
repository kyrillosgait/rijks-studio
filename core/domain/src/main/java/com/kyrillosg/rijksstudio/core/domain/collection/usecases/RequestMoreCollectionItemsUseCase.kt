package com.kyrillosg.rijksstudio.core.domain.collection.usecases

import com.kyrillosg.rijksstudio.core.domain.collection.CollectionRepository
import com.kyrillosg.rijksstudio.core.domain.common.UseCase

class RequestMoreCollectionItemsUseCase(
    private val repository: CollectionRepository,
) : UseCase<RequestMoreCollectionItemsUseCase.Params, Unit> {

    override suspend fun invoke(input: Params) {
        if (input.refreshData) {
            repository.invalidateCollectionItems(input.groupBy)
        }

        repository.requestMoreCollectionItems(input.groupBy)
    }

    data class Params(
        val groupBy: GroupField,
        val refreshData: Boolean,
    )
}