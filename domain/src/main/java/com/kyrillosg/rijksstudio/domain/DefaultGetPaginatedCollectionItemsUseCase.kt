package com.kyrillosg.rijksstudio.domain

import androidx.paging.PagingData
import com.kyrillosg.rijksstudio.core.model.CollectionItem
import com.kyrillosg.rijksstudio.core.data.CollectionRepository
import com.kyrillosg.rijksstudio.core.model.GroupBy
import kotlinx.coroutines.flow.Flow

class DefaultGetPaginatedCollectionItemsUseCase(
    private val repository: CollectionRepository,
) : GetPaginatedCollectionItems {

    override fun invoke(input: GroupBy): Flow<PagingData<CollectionItem>> {
        return repository.getCollectionItemsPaginated(input)
    }
}