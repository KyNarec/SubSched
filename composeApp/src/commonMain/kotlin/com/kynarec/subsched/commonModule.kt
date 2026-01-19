package com.kynarec.subsched

import com.kynarec.shared.SubSchedRepository
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val commonModule: Module = module {
    single { SubSchedRepository(get()) }
    viewModelOf(::SubSchedViewModel)
}