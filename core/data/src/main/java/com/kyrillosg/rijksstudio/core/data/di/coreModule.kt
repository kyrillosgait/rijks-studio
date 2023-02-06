package com.kyrillosg.rijksstudio.core.data.di

import com.kyrillosg.rijksstudio.core.data.CollectionRepository
import com.kyrillosg.rijksstudio.core.data.DefaultCollectionRepository
import com.kyrillosg.rijksstudio.core.data.RijksGateway
import com.kyrillosg.rijksstudio.core.data.fake.FakeRijksGateway
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val coreModule = module {
    singleOf(::DefaultCollectionRepository) {
        bind<CollectionRepository>()
    }

//    single<RijksGateway> {
//        FakeRijksGateway()
//    }
}