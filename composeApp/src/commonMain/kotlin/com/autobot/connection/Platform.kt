package com.autobot.connection

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform