package com.kynarec.subsched

import org.koin.core.module.Module
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

expect val platformModule: Module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(commonModule, platformModule)
}