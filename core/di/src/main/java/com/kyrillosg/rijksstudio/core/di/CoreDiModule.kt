package com.kyrillosg.rijksstudio.core.di

import com.kyrillosg.rijksstudio.core.data.di.dataModule
import com.kyrillosg.rijksstudio.core.data.di.testDataModule
import com.kyrillosg.rijksstudio.core.domain.di.domainModule
import com.kyrillosg.rijksstudio.core.network.di.networkModule
import org.koin.dsl.module

// Links core-modules together without exposing the equivalent gradle modules directly to :app
val coreDiModule = module {
    includes(
        domainModule,
        dataModule,
        networkModule,
    )
}

val testCoreDiModule = module {
    includes(
        domainModule,
        testDataModule,
    )
}
