package com.kyrillosg.rijksstudio.core.data.di

import com.kyrillosg.rijksstudio.core.data.CollectionRepository
import com.kyrillosg.rijksstudio.core.data.fake.FakeCollectionRepository
import org.koin.dsl.module

val dataModule = module {

    single<CollectionRepository> {
        FakeCollectionRepository()
    }
}