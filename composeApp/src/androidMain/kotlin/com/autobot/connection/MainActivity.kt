package com.autobot.connection

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContent {
            FullGamepadControls()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}

@Preview
@Composable
fun GamepadControls() {
    val coroutineScope = rememberCoroutineScope()
    val networkClient = remember { AndroidNetworkClient() } // Keep the network client persistent
    var ipAddress by remember { mutableStateOf("") } // Store IP address
    var showDialog by remember { mutableStateOf(false) } // Control visibility of the input dialog

    // Launch effect to connect to server
    LaunchedEffect(Unit) {
        if (ipAddress.isNotEmpty()) {
            networkClient.connectToServer(ipAddress)
        }
    }

    if (showDialog) {
        // Popup dialog for IP address input
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Enter Server IP Address") },
            text = {
                Column {
                    TextField(
                        value = ipAddress,
                        onValueChange = { ipAddress = it },
                        label = { Text("IP Address") }
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    coroutineScope.launch {
                        networkClient.connectToServer(ipAddress)
                        showDialog = false
                    }
                }) {
                    Text("Connect")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                coroutineScope.launch {
                    networkClient.sendCommandToServer("W_PRESS")
                }
            },
            modifier = Modifier.padding(24.dp)
        ) {
            Text(text = "W")
        }

        Button(
            onClick = {
                coroutineScope.launch {
                    networkClient.sendCommandToServer("A_PRESS")
                }
            },
            modifier = Modifier.padding(24.dp)
        ) {
            Text(text = "A")
        }

        // Add more buttons as needed
    }

    DisposableEffect(Unit) {
        // Close the connection when the UI is disposed
        onDispose {
            coroutineScope.launch {
                networkClient.closeConnection()
            }
        }
    }
}
