package com.autobot.connection

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.autobot.connection.models.LastSean
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.currentKoinScope
import org.koin.core.scope.Scope

@Composable
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = "screenA"
        ) {
            composable("screenA") {
                val viewModel: MainViewModel = koinViewModel()
                val timer by viewModel.timer.collectAsState()
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    val lastSean = remember {
                        listOf(
                            LastSean("New York", TimeZone.of("America/New_York")),
                            LastSean("Kolkata", TimeZone.of("Asia/Kolkata")),
                            LastSean("London", TimeZone.of("Europe/London")),
                        )
                    }

                    var citytime by remember {
                        mutableStateOf(listOf<Pair<LastSean, LocalDateTime>>())
                    }

                    LaunchedEffect(Unit) {
                        while (true) {
                            citytime = lastSean.map { sean ->
                                val now = Clock.System.now()
                                sean to now.toLocalDateTime(sean.timezone)
                            }
                            delay(1000)  // Update every second
                        }
                    }

                    LazyColumn {
                        items(citytime) { (sean, time) ->
                            Text(text = "${sean.name}: $time")
                        }
                    }
                }
            }
        }
    }
}

@Composable
inline fun <reified T: ViewModel> koinViewModel(): T {
    val scope = currentKoinScope()
    return viewModel {
        scope.get<T>()
    }
}
