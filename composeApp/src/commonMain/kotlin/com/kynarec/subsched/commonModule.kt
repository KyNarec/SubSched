package com.kynarec.subsched

import com.kynarec.shared.SubSchedRepository
import org.koin.core.module.Module
import org.koin.dsl.module

val commonModule: Module = module {
    single { SubSchedRepository(get(), get()) }
    single { SubSchedViewModel(get()) }
}