package com.kyrillosg.rijksstudio.core.domain.collection.usecases

import com.kyrillosg.rijksstudio.core.domain.collection.CollectionRepository
import com.kyrillosg.rijksstudio.core.domain.collection.model.CollectionItem
import com.kyrillosg.rijksstudio.core.domain.collection.model.DetailedCollectionItem
import com.kyrillosg.rijksstudio.core.domain.common.UseCase

class GetDetailedCollectionItemUseCase(
    private val repository: CollectionRepository,
) : UseCase<CollectionItem.Id, DetailedCollectionItem> {

    override suspend fun invoke(input: CollectionItem.Id): DetailedCollectionItem {
        return repository.getDetailedCollectionItem(id = input)
    }
}
