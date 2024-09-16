package com.autobot.connection

import android.app.Application

class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()
        KoinInitializer(applicationContext).init()
    }
}