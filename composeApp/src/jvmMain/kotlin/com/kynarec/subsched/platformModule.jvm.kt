package com.kynarec.subsched

import eu.anifantakis.lib.ksafe.KSafe
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import org.koin.dsl.module
import org.koin.core.module.Module

actual val platformModule = module {
    single { KSafe() }

    single {
        HttpClient(CIO)
    }
}