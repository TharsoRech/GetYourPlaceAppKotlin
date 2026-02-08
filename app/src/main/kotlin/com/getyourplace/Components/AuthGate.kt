package com.getyourplace.Components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp
import com.getyourplace.Managers.AuthManager
import androidx.compose.runtime.getValue

@Composable
fun AuthGate(
    auth: AuthManager,
    content: @Composable () -> Unit
) {
    // 1. Convert StateFlow<Boolean> into a regular Boolean
    // You'll need: import androidx.compose.runtime.collectAsState
    val isAuthenticated by auth.isAuthenticated.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .blur(if (isAuthenticated) 0.dp else 5.dp) // Now 'isAuthenticated' is a Boolean!
        ) {
            content()
        }

        AnimatedVisibility(
            visible = !isAuthenticated, // Now '!' works!
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            val popupState = remember { mutableStateOf(true) }
            LoginPopupView(
                isPresented = popupState,
                canClose = false,
                auth = auth
            )
        }
    }
}