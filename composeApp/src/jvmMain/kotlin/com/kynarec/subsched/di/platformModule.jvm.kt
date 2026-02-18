package com.kynarec.subsched.di

import eu.anifantakis.lib.ksafe.KSafe
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import org.koin.dsl.module

actual val platformModule = module {
    single { KSafe() }

    single {
        HttpClient(CIO) {
            expectSuccess = true
        }
    }
}