package com.kyrillosg.rijksstudio.domain

import com.kyrillosg.rijksstudio.core.model.CollectionItem
import com.kyrillosg.rijksstudio.core.data.CollectionRepository
import com.kyrillosg.rijksstudio.core.model.DetailedCollectionItem

class DefaultGetDetailedCollectionItemUseCase(
    private val repository: CollectionRepository,
) : GetDetailedCollectionItemUseCase {

    override suspend fun invoke(input: CollectionItem.Id): DetailedCollectionItem? {
        return repository.getDetailedCollectionItem(id = input)
    }
}