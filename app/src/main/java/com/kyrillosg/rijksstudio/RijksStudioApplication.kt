package com.kyrillosg.rijksstudio

import android.app.Application
import com.kyrillosg.rijksstudio.core.di.coreModule
import com.kyrillosg.rijksstudio.domain.di.domainModule
import com.kyrillosg.rijksstudio.feature.collection.di.collectionFeatureModule
import com.kyrillosg.rijksstudio.network.di.networkModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

val rijksStudioModule = module {
    includes(
        coreModule,
        networkModule,
        domainModule,
        collectionFeatureModule,
    )
}

class RijksStudioApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@RijksStudioApplication)
            modules(rijksStudioModule)
        }

        if (BuildConfig.DEBUG) {
            Napier.base(DebugAntilog())
        }
    }
}