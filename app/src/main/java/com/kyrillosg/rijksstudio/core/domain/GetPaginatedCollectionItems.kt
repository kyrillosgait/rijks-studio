package com.kyrillosg.rijksstudio.core.domain

import androidx.paging.PagingData
import com.kyrillosg.rijksstudio.core.data.CollectionItem
import com.kyrillosg.rijksstudio.core.domain.common.SynchronousUseCase
import kotlinx.coroutines.flow.Flow

interface GetPaginatedCollectionItems : SynchronousUseCase<Unit, Flow<PagingData<CollectionItem>>>