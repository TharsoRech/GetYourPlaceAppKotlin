package com.getyourplace.Components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp
import com.getyourplace.Managers.AuthManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun AuthGate(auth: AuthManager, content: @Composable () -> Unit) {
    val isAuthenticated by auth.isAuthenticated.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        // 1. Background Content - ONLY render if authenticated
        // OR handle the ViewModel crash separately.
        Box(modifier = Modifier
            .fillMaxSize()
            .blur(if (isAuthenticated) 0.dp else 10.dp)
        ) {
            // Recommendation: Only run content() if authenticated to avoid
            // the MyRentsViewModel crash
            if (isAuthenticated) {
                content()
            } else {
                // Optional: Show a placeholder or empty state under the blur
                // so the app doesn't try to load protected data/ViewModels
            }
        }

        // 2. Auth Overlay
        AnimatedVisibility(
            visible = !isAuthenticated,
            enter = fadeIn() + slideInVertically { it / 2 },
            exit = fadeOut() + slideOutVertically { it / 2 }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f)), // Darken for visibility
                contentAlignment = Alignment.Center
            ) {
                LoginPopupView(
                    isPresented = remember { mutableStateOf(true) },
                    canClose = false,
                    auth = auth
                )
            }
        }
    }
}


