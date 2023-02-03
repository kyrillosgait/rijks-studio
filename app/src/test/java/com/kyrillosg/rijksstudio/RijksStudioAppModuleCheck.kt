package com.kyrillosg.rijksstudio

import android.content.Context
import io.ktor.client.*
import io.ktor.client.engine.*
import org.junit.jupiter.api.Test
import org.koin.test.verify.verify

class RijksStudioAppModuleCheck {

    @Test
    fun `dependency injection definitions are defined`() {
        rijksStudioModule.verify(
            extraTypes = listOf(
                Context::class,

                // False positives
                String::class,
                HttpClientEngine::class,
                HttpClientConfig::class,
            )
        )
    }
}