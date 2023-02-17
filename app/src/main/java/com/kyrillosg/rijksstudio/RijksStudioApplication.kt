package com.kyrillosg.rijksstudio

import android.app.Application
import com.kyrillosg.rijksstudio.core.di.coreDiModule
import com.kyrillosg.rijksstudio.feature.collection.di.collectionFeatureModule
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

val rijksStudioModule = module {
    includes(
        coreDiModule,
        collectionFeatureModule,
    )
}

class RijksStudioApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        AppCenter.start(
            this,
            BuildConfig.APP_CENTER_SECRET,
            Analytics::class.java, Crashes::class.java
        )

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