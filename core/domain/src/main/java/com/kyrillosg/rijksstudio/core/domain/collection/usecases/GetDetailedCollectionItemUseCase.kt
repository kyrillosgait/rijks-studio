package com.kyrillosg.rijksstudio.core.domain.collection.usecases

import com.kyrillosg.rijksstudio.core.domain.collection.model.CollectionItem
import com.kyrillosg.rijksstudio.core.domain.collection.model.DetailedCollectionItem
import com.kyrillosg.rijksstudio.core.domain.common.UseCase

interface GetDetailedCollectionItemUseCase : UseCase<CollectionItem.Id, DetailedCollectionItem?>
