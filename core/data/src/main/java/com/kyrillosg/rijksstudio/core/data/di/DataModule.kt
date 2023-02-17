package com.kyrillosg.rijksstudio.core.data.di

import com.kyrillosg.rijksstudio.core.data.DefaultCollectionRepository
import com.kyrillosg.rijksstudio.core.domain.collection.CollectionRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule = module {
    singleOf(::DefaultCollectionRepository) {
        bind<CollectionRepository>()
    }

//    single<RijksGateway> {
//        FakeRijksGateway()
//    }
}
