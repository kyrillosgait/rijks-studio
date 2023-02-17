package com.kyrillosg.rijksstudio.core.domain.collection.usecases

import androidx.paging.PagingData
import com.kyrillosg.rijksstudio.core.domain.collection.model.CollectionItem
import com.kyrillosg.rijksstudio.core.domain.collection.model.GroupBy
import com.kyrillosg.rijksstudio.core.domain.common.SynchronousUseCase
import kotlinx.coroutines.flow.Flow

interface GetPaginatedCollectionItems : SynchronousUseCase<GroupBy, Flow<PagingData<CollectionItem>>>
