package com.autobot.connection

// Shared code (commonMain)
interface NetworkClient {
    suspend fun sendCommandToServer(command: String)
}
