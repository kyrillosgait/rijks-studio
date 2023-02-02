package com.kyrillosg.rijksstudio.core.data.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.kyrillosg.rijksstudio.BuildConfig
import com.kyrillosg.rijksstudio.core.data.RijksService
import com.kyrillosg.rijksstudio.core.data.network.DefaultRijksService
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule = module {
    singleOf(::provideHttpClient)

    singleOf(::DefaultRijksService) {
        bind<RijksService>()
    }

    single {
        NetworkConfiguration(
            baseUrl = "https://www.rijksmuseum.nl/api",
            apiKey = BuildConfig.API_KEY,
        )
    }
}

data class NetworkConfiguration(
    val baseUrl: String,
    val apiKey: String,
)

private fun provideHttpClient(context: Context): HttpClient {
    return HttpClient(OkHttp) {
        expectSuccess = true
        install(Logging) {
            level = LogLevel.ALL
            logger = object : Logger {
                override fun log(message: String) {
                    Napier.v { message }
                }
            }
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            })
        }
        engine {
            addInterceptor(ChuckerInterceptor.Builder(context).build())
        }
    }
}
