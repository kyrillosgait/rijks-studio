package com.kyrillosg.rijksstudio.core.domain.di

import com.kyrillosg.rijksstudio.core.domain.collection.usecases.*
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {

    factoryOf(::GetGroupedCollectionStreamUseCase)
    factoryOf(::RequestMoreCollectionItemsUseCase)
    factoryOf(::GetDetailedCollectionItemUseCase)
}
