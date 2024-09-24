package com.autobot.connection

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import org.koin.dsl.module


val appModule = module {
    single { "Hello world!" }
    // Provide AuthService instance
    single<AuthService> { AuthServiceImpl(Firebase.auth) }
}