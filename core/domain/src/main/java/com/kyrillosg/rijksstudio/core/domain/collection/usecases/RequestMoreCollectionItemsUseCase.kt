package com.kyrillosg.rijksstudio.core.domain.collection.usecases

import com.kyrillosg.rijksstudio.core.domain.collection.CollectionRepository
import com.kyrillosg.rijksstudio.core.domain.collection.model.GroupBy
import com.kyrillosg.rijksstudio.core.domain.common.UseCase

class RequestMoreCollectionItemsUseCase(
    private val repository: CollectionRepository,
) : UseCase<GroupBy, Unit> {

    override suspend fun invoke(input: GroupBy) {
        repository.requestMoreCollectionItems(input)
    }
}
