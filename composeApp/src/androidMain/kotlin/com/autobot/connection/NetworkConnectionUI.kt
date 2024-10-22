package com.autobot.connection

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun NetworkConnectionUI() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val networkClient = remember { AndroidNetworkClient() }

    var ipAddress by remember { mutableStateOf("") }
    var isConnected by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Input field for IP address
        Text(text = "Enter Server IP Address:")
        Spacer(modifier = Modifier.height(8.dp))

        BasicTextField(
            value = ipAddress,
            onValueChange = { ipAddress = it },
            modifier = Modifier
                .background(Color.LightGray)
                .padding(8.dp)
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number) // Numeric keyboard
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Button to connect to the server
        Button(onClick = {
            if (ipAddress.isNotEmpty()) {
                coroutineScope.launch {
                    networkClient.connectToServer(ipAddress)
                    isConnected = true
                    Toast.makeText(context, "Connected to $ipAddress", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Please enter a valid IP address", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text(text = if (isConnected) "Connected" else "Connect")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button to disconnect
        Button(onClick = {
            coroutineScope.launch {
                networkClient.closeConnection()
                isConnected = false
                Toast.makeText(context, "Connection closed", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text(text = "Disconnect")
        }
    }
}
