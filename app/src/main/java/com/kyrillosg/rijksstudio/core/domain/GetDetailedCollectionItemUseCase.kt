package com.kyrillosg.rijksstudio.core.domain

import com.kyrillosg.rijksstudio.core.model.CollectionItem
import com.kyrillosg.rijksstudio.core.model.DetailedCollectionItem
import com.kyrillosg.rijksstudio.core.domain.common.UseCase

interface GetDetailedCollectionItemUseCase: UseCase<CollectionItem.Id, DetailedCollectionItem?>