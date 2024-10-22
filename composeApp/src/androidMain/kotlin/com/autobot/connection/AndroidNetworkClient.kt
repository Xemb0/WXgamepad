package com.autobot.connection// Android-specific code (androidMain)

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.OutputStream
import java.net.Socket

class AndroidNetworkClient {
    private var socket: Socket? = null
    private var outputStream: OutputStream? = null

    // Establish the connection once with user-provided IP address
    suspend fun connectToServer(ipAddress: String) {
        withContext(Dispatchers.IO) {
            try {
                socket = Socket(ipAddress, 8080)  // Use the provided IP address
                outputStream = socket?.getOutputStream()
            } catch (e: Exception) {
                e.printStackTrace()  // Log connection error
            }
        }
    }

    // Send command over the already established connection
    suspend fun sendCommandToServer(command: String) {
        withContext(Dispatchers.IO) {
            try {
                outputStream?.write(command.toByteArray())
                outputStream?.flush()
            } catch (e: Exception) {
                e.printStackTrace()  // Log send command error
            }
        }
    }

    // Close the connection when done
    suspend fun closeConnection() {
        withContext(Dispatchers.IO) {
            try {
                outputStream?.close()
                socket?.close()
            } catch (e: Exception) {
                e.printStackTrace()  // Log close connection error
            }
        }
    }
}
