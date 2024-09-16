package com.autobot.connection

import org.koin.dsl.module

val appModule = module {
    single { "Hello world!" }
}