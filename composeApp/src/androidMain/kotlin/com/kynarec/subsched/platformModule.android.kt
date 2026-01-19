package com.kynarec.subsched

import eu.anifantakis.lib.ksafe.KSafe
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

actual val platformModule = module {
    single { KSafe(androidApplication()) }

    single {
        HttpClient(OkHttp) {
            expectSuccess = true
        }
    }
}