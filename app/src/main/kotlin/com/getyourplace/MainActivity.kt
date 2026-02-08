package com.getyourplace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import com.getyourplace.Persistence.AppDatabase
import com.getyourplace.Persistence.ItemRepository
import com.getyourplace.Components.SplashScreenView
import com.getyourplace.Views.Pages.HomeScreen

import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Initialize Data Layer
        val database = AppDatabase.getDatabase(applicationContext)
        val itemRepository = ItemRepository(database.itemDao())

        // 2. Set the UI Content
        setContent {
            // State for navigation logic
            var isActive by remember { mutableStateOf(false) }

            // Splash timer logic
            LaunchedEffect(Unit) {
                delay(2500)
                isActive = true
            }

            // Transition Animation
            Crossfade(
                targetState = isActive,
                animationSpec = tween(durationMillis = 500),
                label = "SplashToHome"
            ) { screenActive ->
                if (screenActive) {
                    HomeScreen(itemRepository)
                } else {
                    SplashScreenView()
                }
            }
        }
    }
}