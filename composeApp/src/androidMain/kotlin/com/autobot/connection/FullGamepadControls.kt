package com.autobot.connection

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@Preview(
    showBackground = true,
    widthDp = 4698 / 3,  // Convert pixels to dp for the preview
    heightDp = 2412 / 3  // Convert pixels to dp for the preview
)
@Composable
fun FullGamepadControls() {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current // Get the current context
    val networkClient = remember { AndroidNetworkClient() }

    // Get screen dimensions
    val configuration = LocalConfiguration.current
    val screenWidth = with(LocalDensity.current) { configuration.screenWidthDp.dp.toPx() }
    val screenHeight = with(LocalDensity.current) { configuration.screenHeightDp.dp.toPx() }

    // State to store the positions of buttons, edit mode, connection state, and IP address
    var buttonPositions by remember { mutableStateOf(loadButtonPositions(screenWidth, screenHeight)) }
    var isEditMode by remember { mutableStateOf(false) }
    var ipAddress by remember { mutableStateOf("") }
    var isConnected by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onKeyEvent { keyEvent ->
                if (keyEvent.type == KeyEventType.KeyDown) {
                    // Map keyboard keys to button actions
                    when (keyEvent.key) {
                        Key.W -> {
                            coroutineScope.launch {
                                networkClient.sendCommandToServer("W_PRESS")
                                Toast.makeText(context, "W Pressed", Toast.LENGTH_SHORT).show() // Toast message
                            }
                        }
                        Key.A -> {
                            coroutineScope.launch {
                                networkClient.sendCommandToServer("A_PRESS")
                                Toast.makeText(context, "A Pressed", Toast.LENGTH_SHORT).show() // Toast message
                            }
                        }
                        Key.S -> {
                            coroutineScope.launch {
                                networkClient.sendCommandToServer("S_PRESS")
                                Toast.makeText(context, "S Pressed", Toast.LENGTH_SHORT).show() // Toast message
                            }
                        }
                        Key.D -> {
                            coroutineScope.launch {
                                networkClient.sendCommandToServer("D_PRESS")
                                Toast.makeText(context, "D Pressed", Toast.LENGTH_SHORT).show() // Toast message
                            }
                        }
                        Key.Spacebar -> {
                            coroutineScope.launch {
                                networkClient.sendCommandToServer("SPACE_PRESS")
                                Toast.makeText(context, "Space Pressed", Toast.LENGTH_SHORT).show() // Toast message
                            }
                        }
                        Key.ShiftLeft -> {
                            coroutineScope.launch {
                                networkClient.sendCommandToServer("SHIFT_PRESS")
                                Toast.makeText(context, "Shift Pressed", Toast.LENGTH_SHORT).show() // Toast message
                            }
                        }
                        Key.Enter -> {
                            coroutineScope.launch {
                                networkClient.sendCommandToServer("ENTER_PRESS")
                                Toast.makeText(context, "Enter Pressed", Toast.LENGTH_SHORT).show() // Toast message
                            }
                        }
                        Key.Escape -> {
                            coroutineScope.launch {
                                networkClient.sendCommandToServer("ESC_PRESS")
                                Toast.makeText(context, "Esc Pressed", Toast.LENGTH_SHORT).show() // Toast message
                            }
                        }
                    }
                }
                false // Return false to let other handlers process the event
            }
    ) {
        buttonPositions.forEach { (buttonName, position) ->
            DraggableButton(
                text = buttonName,
                position = position,
                onPositionChange = { newPosition ->
                    if (isEditMode) {
                        buttonPositions = buttonPositions.toMutableMap().apply {
                            put(buttonName, newPosition) // Directly set the new position
                        }
                    }
                },
                onClick = {
                    if (!isEditMode) { // Allow clicking only when not in edit mode
                        coroutineScope.launch {
                            networkClient.sendCommandToServer(buttonName)
                            Toast.makeText(context, "$buttonName Pressed", Toast.LENGTH_SHORT).show() // Toast message
                        }
                    }
                },
                isEditMode = isEditMode // Pass edit mode state
            )
        }

        // Joystick
        Joystick(
            onMove = { direction -> coroutineScope.launch { networkClient.sendCommandToServer(direction) } },
            screenWidth = screenWidth,
            screenHeight = screenHeight // Pass screen dimensions to the joystick
        )

        // Connect button
        Button(
            onClick = {
                if (ipAddress.isEmpty()) {
                    showDialog = true // Show dialog to enter IP address
                } else {
                    coroutineScope.launch {
                        try {
                            networkClient.connectToServer(ipAddress)
                            isConnected = true
                            Toast.makeText(context, "Connected to $ipAddress", Toast.LENGTH_SHORT).show() // Toast message
                        } catch (e: Exception) {
                            // Handle connection failure
                            isConnected = false
                            Toast.makeText(context, "Connection failed", Toast.LENGTH_SHORT).show() // Toast message
                        }
                    }
                }
            },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .pointerInput(Unit) {
                    detectTapGestures(onLongPress = {
                        showDialog = true // Show dialog to edit IP address on long press
                    })
                },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isConnected) Color.Green else Color.Red
            )
        ) {
            Text("Connect")
        }

        // Show dialog for IP address input
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Enter IP Address") },
                text = {
                    TextField(
                        value = ipAddress,
                        onValueChange = { ipAddress = it },
                        label = { Text("IP Address") }
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showDialog = false // Close dialog
                            if (ipAddress.isNotEmpty()) {
                                coroutineScope.launch {
                                    try {
                                        networkClient.connectToServer(ipAddress)
                                        isConnected = true
                                        Toast.makeText(context, "Connected to $ipAddress", Toast.LENGTH_SHORT).show() // Toast message
                                    } catch (e: Exception) {
                                        isConnected = false
                                        Toast.makeText(context, "Connection failed", Toast.LENGTH_SHORT).show() // Toast message
                                    }
                                }
                            }
                        }
                    ) {
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

        // Edit mode toggle button
        EditModeToggleButton(
            isEditMode = isEditMode,
            onToggle = {
                isEditMode = !isEditMode
                if (!isEditMode) {
                    // Reset to default layout when exiting edit mode
                    buttonPositions = loadButtonPositions(screenWidth, screenHeight)
                }
            }
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            coroutineScope.launch {
                networkClient.closeConnection()
            }
        }
    }
}

@Composable
fun DraggableButton(
    text: String,
    position: Offset,
    onPositionChange: (Offset) -> Unit,
    onClick: () -> Unit,
    isEditMode: Boolean // New parameter
) {
    var offset by remember { mutableStateOf(position) }
    var isDragging by remember { mutableStateOf(false) }
    var initialOffset by remember { mutableStateOf(Offset(0f, 0f)) }

    // Use LocalDensity to convert Dp to pixels
    val density = LocalDensity.current
    val screenWidth = with(density) { LocalConfiguration.current.screenWidthDp.dp.toPx() }
    val screenHeight = with(density) { LocalConfiguration.current.screenHeightDp.dp.toPx() }

    Box(
        modifier = Modifier
            .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { initialOffset = offset },
                    onDrag = { change, dragAmount ->
                        if (isEditMode) {
                            change.consume() // Consume the drag change
                            isDragging = true // Set dragging state
                            offset = Offset(
                                x = (offset.x + dragAmount.x).coerceIn(0f, screenWidth - with(density) { 80.dp.toPx() }),
                                y = (offset.y + dragAmount.y).coerceIn(0f, screenHeight - with(density) { 80.dp.toPx() })
                            )
                            onPositionChange(offset) // Notify the position change
                        }
                    },
                    onDragEnd = {
                        isDragging = false // Reset dragging state
                    }
                )
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        if (!isDragging) { // Only trigger click if not dragging
                            onClick()
                        }
                    }
                )
            }
            .background(if (isEditMode) Color.LightGray else Color.Gray, shape = CircleShape)
            .size(60.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text)
    }
}


fun loadButtonPositions(screenWidth: Float, screenHeight: Float): MutableMap<String, Offset> {
    return mutableMapOf(
        "^" to Offset(screenWidth * 0.25f, screenHeight * 0.15f),   // W key
        "<" to Offset(screenWidth * 0.17f, screenHeight * 0.3f), // A key
        "v" to Offset(screenWidth * 0.25f, screenHeight * 0.45f), // S key
        ">" to Offset(screenWidth * 0.33f, screenHeight * 0.3f), // D key
        "X" to Offset(screenWidth * 0.8f, screenHeight * 0.7f),    // Space key
        "O" to Offset(screenWidth * 0.87f, screenHeight * 0.55f),    // Shift key
        "[]" to Offset(screenWidth * 0.73f, screenHeight * 0.55f),    // Enter key
        "V" to Offset(screenWidth * 0.8f, screenHeight * 0.4f),    // Esc key
        "L1" to Offset(screenWidth * 0.15f, screenHeight * 0.05f),     // Left Bumper (could be another key)
        "L2" to Offset(screenWidth * 0.8f, screenHeight * 0.05f),    // Left Trigger (could be another key)
        "R1" to Offset(screenWidth * 0.05f, screenHeight * 0.2f),    // Right Bumper (could be another key)
        "R2" to Offset(screenWidth * 0.9f, screenHeight * 0.2f),    // Right Trigger (could be another key)
        "START" to Offset(screenWidth * 0.4f, screenHeight * 0.8f)  // Start button (could be another key)
    )
}


// Add a Composable for the Edit Mode Toggle Button
@Composable
fun EditModeToggleButton(isEditMode: Boolean, onToggle: () -> Unit) {
    Box(
        modifier = Modifier
            .size(30.dp)
            .background(Color.Blue, shape = CircleShape)
            .pointerInput(Unit) {
                detectTapGestures(onTap = { onToggle() })
            },
        contentAlignment = Alignment.Center
    ) {
        Text(if (isEditMode) "Save" else "Edit", color = Color.White)
    }
}

@Composable
fun Joystick(
    onMove: (String) -> Unit,
    screenWidth: Float,
    screenHeight: Float
) {
    // Set the default position for the joystick container
    val initialContainerOffsetX = screenWidth * 0.1f // Adjust this based on where you want the joystick
    val initialContainerOffsetY = screenHeight * 0.6f // Adjust for vertical position

    // Internal joystick offset
    var internalOffsetX by remember { mutableStateOf(0f) }
    var internalOffsetY by remember { mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .size(120.dp)
            .offset { IntOffset(initialContainerOffsetX.roundToInt(), initialContainerOffsetY.roundToInt()) } // Position the container
            .background(Color.LightGray, shape = CircleShape)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        internalOffsetX += dragAmount.x
                        internalOffsetY += dragAmount.y

                        // Trigger movements based on direction
                        when {
                            internalOffsetY < -50 -> onMove("W")  // Send W for UP
                            internalOffsetY > 50 -> onMove("S")   // Send S for DOWN
                            internalOffsetX < -50 -> onMove("A")  // Send A for LEFT
                            internalOffsetX > 50 -> onMove("D")   // Send D for RIGHT
                        }
                    },
                    onDragEnd = {
                        // Reset the internal joystick position
                        internalOffsetX = 0f
                        internalOffsetY = 0f
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .offset { IntOffset(internalOffsetX.roundToInt(), internalOffsetY.roundToInt()) } // Internal joystick offset
                .background(Color.DarkGray, shape = CircleShape)
        )
    }
}


