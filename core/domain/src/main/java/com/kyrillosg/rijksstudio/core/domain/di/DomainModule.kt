package com.kyrillosg.rijksstudio.core.domain.di

import com.kyrillosg.rijksstudio.core.domain.DefaultGetDetailedCollectionItemUseCase
import com.kyrillosg.rijksstudio.core.domain.DefaultGetPaginatedCollectionItemsUseCase
import com.kyrillosg.rijksstudio.core.domain.GetDetailedCollectionItemUseCase
import com.kyrillosg.rijksstudio.core.domain.GetPaginatedCollectionItems
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
