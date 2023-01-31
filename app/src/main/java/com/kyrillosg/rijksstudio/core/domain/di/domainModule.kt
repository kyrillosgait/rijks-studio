package com.kyrillosg.rijksstudio.core.domain.di

import com.kyrillosg.rijksstudio.core.domain.DefaultGetPaginatedCollectionItemsUseCase
import com.kyrillosg.rijksstudio.core.domain.DefaultGetCollectionItemsUseCase
import com.kyrillosg.rijksstudio.core.domain.GetCollectionItemsUseCase
import com.kyrillosg.rijksstudio.core.domain.GetPaginatedCollectionItems
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {

    factoryOf(::DefaultGetCollectionItemsUseCase) {
        bind<GetCollectionItemsUseCase>()
    }

    factoryOf(::DefaultGetPaginatedCollectionItemsUseCase) {
        bind<GetPaginatedCollectionItems>()
    }
}