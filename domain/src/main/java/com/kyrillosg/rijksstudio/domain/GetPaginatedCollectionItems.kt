package com.kyrillosg.rijksstudio.domain

import androidx.paging.PagingData
import com.kyrillosg.rijksstudio.core.model.CollectionItem
import com.kyrillosg.rijksstudio.domain.common.SynchronousUseCase
import com.kyrillosg.rijksstudio.core.model.GroupBy
import kotlinx.coroutines.flow.Flow

interface GetPaginatedCollectionItems : SynchronousUseCase<GroupBy, Flow<PagingData<CollectionItem>>>