package com.kyrillosg.rijksstudio.core.domain

import androidx.paging.PagingData
import com.kyrillosg.rijksstudio.core.data.model.CollectionItem
import com.kyrillosg.rijksstudio.core.data.CollectionRepository
import kotlinx.coroutines.flow.Flow

class DefaultGetPaginatedCollectionItemsUseCase(
    private val repository: CollectionRepository,
) : GetPaginatedCollectionItems {

    override fun invoke(input: Unit): Flow<PagingData<CollectionItem>> {
        return repository.getCollectionItemsPaginated()
    }
}