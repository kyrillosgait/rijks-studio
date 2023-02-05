package com.kyrillosg.rijksstudio.core.domain

import androidx.paging.PagingData
import com.kyrillosg.rijksstudio.core.data.model.CollectionItem
import com.kyrillosg.rijksstudio.core.data.CollectionRepository
import com.kyrillosg.rijksstudio.core.data.model.GroupBy
import kotlinx.coroutines.flow.Flow

internal class DefaultGetPaginatedCollectionItemsUseCase(
    private val repository: CollectionRepository,
) : GetPaginatedCollectionItems {

    override fun invoke(input: GroupBy): Flow<PagingData<CollectionItem>> {
        return repository.getCollectionItemsPaginated(input)
    }
}