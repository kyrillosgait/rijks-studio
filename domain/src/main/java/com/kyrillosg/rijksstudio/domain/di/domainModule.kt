package com.kyrillosg.rijksstudio.domain.di

import com.kyrillosg.rijksstudio.domain.DefaultGetDetailedCollectionItemUseCase
import com.kyrillosg.rijksstudio.domain.DefaultGetPaginatedCollectionItemsUseCase
import com.kyrillosg.rijksstudio.domain.GetDetailedCollectionItemUseCase
import com.kyrillosg.rijksstudio.domain.GetPaginatedCollectionItems
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {

    factoryOf(::DefaultGetPaginatedCollectionItemsUseCase) {
        bind<GetPaginatedCollectionItems>()
    }

    factoryOf(::DefaultGetDetailedCollectionItemUseCase) {
        bind<GetDetailedCollectionItemUseCase>()
    }
}