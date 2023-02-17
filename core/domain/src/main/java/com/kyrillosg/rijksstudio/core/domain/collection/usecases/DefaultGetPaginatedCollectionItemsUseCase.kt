package com.kyrillosg.rijksstudio.core.domain.collection.usecases

import androidx.paging.PagingData
import com.kyrillosg.rijksstudio.core.domain.collection.CollectionRepository
import com.kyrillosg.rijksstudio.core.domain.collection.model.CollectionItem
import com.kyrillosg.rijksstudio.core.domain.collection.model.GroupBy
import kotlinx.coroutines.flow.Flow

internal class DefaultGetPaginatedCollectionItemsUseCase(
    private val repository: CollectionRepository,
) : GetPaginatedCollectionItems {

    override fun invoke(input: GroupBy): Flow<PagingData<CollectionItem>> {
        return repository.getCollectionItemsPaginated(input)
    }
}
