package com.kyrillosg.rijksstudio.core.domain

import com.kyrillosg.rijksstudio.core.data.CollectionItem
import com.kyrillosg.rijksstudio.core.data.DetailedCollectionItem
import com.kyrillosg.rijksstudio.core.domain.common.UseCase

interface GetDetailedCollectionItemUseCase: UseCase<CollectionItem.Id, DetailedCollectionItem?>