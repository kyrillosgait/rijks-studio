package com.kyrillosg.rijksstudio.core.di

import com.kyrillosg.rijksstudio.core.data.CollectionRepository
import com.kyrillosg.rijksstudio.core.data.DefaultCollectionRepository
import com.kyrillosg.rijksstudio.core.data.RijksGateway
import com.kyrillosg.rijksstudio.core.fake.FakeRijksGateway
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val coreModule = module {
    singleOf(::DefaultCollectionRepository) {
        bind<CollectionRepository>()
    }

    singleOf(::FakeRijksGateway) {
        bind<RijksGateway>()
    }
}