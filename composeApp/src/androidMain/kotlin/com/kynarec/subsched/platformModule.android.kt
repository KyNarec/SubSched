package com.kynarec.subsched

import eu.anifantakis.lib.ksafe.KSafe
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import org.koin.core.module.Module

actual val platformModule = module {
    single { KSafe(androidApplication()) }

    single {
        HttpClient(OkHttp) {
        }
    }
}