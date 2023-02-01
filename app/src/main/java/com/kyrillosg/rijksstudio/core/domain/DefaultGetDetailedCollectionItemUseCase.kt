package com.kyrillosg.rijksstudio.core.domain

import com.kyrillosg.rijksstudio.core.data.model.CollectionItem
import com.kyrillosg.rijksstudio.core.data.CollectionRepository
import com.kyrillosg.rijksstudio.core.data.model.DetailedCollectionItem

class DefaultGetDetailedCollectionItemUseCase(
    private val repository: CollectionRepository,
) : GetDetailedCollectionItemUseCase {

    override suspend fun invoke(input: CollectionItem.Id): DetailedCollectionItem? {
        return repository.getDetailedCollectionItem(id = input)
    }
}