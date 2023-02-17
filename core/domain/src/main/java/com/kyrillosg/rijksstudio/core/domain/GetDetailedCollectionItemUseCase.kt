package com.kyrillosg.rijksstudio.core.domain

import com.kyrillosg.rijksstudio.core.data.model.CollectionItem
import com.kyrillosg.rijksstudio.core.data.model.DetailedCollectionItem
import com.kyrillosg.rijksstudio.core.domain.common.UseCase

interface GetDetailedCollectionItemUseCase : UseCase<CollectionItem.Id, DetailedCollectionItem?>
