package com.kyrillosg.rijksstudio

import android.app.Application
import com.kyrillosg.rijksstudio.core.di.testCoreDiModule
import com.kyrillosg.rijksstudio.feature.collection.di.collectionFeatureModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

val testRijksStudioModule = module {
    includes(
        testCoreDiModule,
        collectionFeatureModule,
    )
}

class TestRijksStudioApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@TestRijksStudioApplication)
            modules(testRijksStudioModule)
        }
    }
}
