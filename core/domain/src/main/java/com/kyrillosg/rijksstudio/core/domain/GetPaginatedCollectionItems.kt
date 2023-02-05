package com.kyrillosg.rijksstudio.core.domain

import androidx.paging.PagingData
import com.kyrillosg.rijksstudio.core.data.model.CollectionItem
import com.kyrillosg.rijksstudio.core.domain.common.SynchronousUseCase
import com.kyrillosg.rijksstudio.core.data.model.GroupBy
import kotlinx.coroutines.flow.Flow

interface GetPaginatedCollectionItems : SynchronousUseCase<GroupBy, Flow<PagingData<CollectionItem>>>