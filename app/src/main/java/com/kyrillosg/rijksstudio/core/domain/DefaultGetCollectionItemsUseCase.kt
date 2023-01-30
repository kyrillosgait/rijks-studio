package com.kyrillosg.rijksstudio.core.domain

import com.kyrillosg.rijksstudio.core.data.CollectionItem
import com.kyrillosg.rijksstudio.core.data.CollectionRepository

class DefaultGetCollectionItemsUseCase(
    private val repository: CollectionRepository,
) : GetCollectionItemsUseCase {

    override suspend fun invoke(input: Unit): List<CollectionItem> {
        return repository.getCollectionItems()
    }
}