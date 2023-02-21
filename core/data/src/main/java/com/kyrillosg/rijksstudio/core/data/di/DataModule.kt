package com.kyrillosg.rijksstudio.core.data.di

import com.kyrillosg.rijksstudio.core.data.DefaultCollectionRepository
import com.kyrillosg.rijksstudio.core.domain.collection.CollectionRepository
import org.koin.dsl.module

val dataModule = module {
    single<CollectionRepository> {
        DefaultCollectionRepository(rijksGateway = get())
    }
}
