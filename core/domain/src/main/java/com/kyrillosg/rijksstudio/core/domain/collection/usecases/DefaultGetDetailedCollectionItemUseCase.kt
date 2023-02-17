package com.kyrillosg.rijksstudio.core.domain.collection.usecases

import com.kyrillosg.rijksstudio.core.domain.collection.CollectionRepository
import com.kyrillosg.rijksstudio.core.domain.collection.model.CollectionItem
import com.kyrillosg.rijksstudio.core.domain.collection.model.DetailedCollectionItem

internal class DefaultGetDetailedCollectionItemUseCase(
    private val repository: CollectionRepository,
) : GetDetailedCollectionItemUseCase {

    override suspend fun invoke(input: CollectionItem.Id): DetailedCollectionItem? {
        return repository.getDetailedCollectionItem(id = input)
    }
}
