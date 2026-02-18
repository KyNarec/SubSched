package com.kynarec.subsched.di

import com.kynarec.shared.SubSchedRepository
import com.kynarec.subsched.SubSchedViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val commonModule: Module = module {
    single { SubSchedRepository(get(), get()) }
    single { SubSchedViewModel(get()) }
}