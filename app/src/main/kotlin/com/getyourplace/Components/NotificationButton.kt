package com.getyourplace.Components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

@Composable
fun NotificationButton(
    notifications: List<String>,
    onNotificationUpdate: (List<String>) -> Unit
) {
    var showList by remember { mutableStateOf(false) }

    // Use a Box to replicate the ZStack (alignment: .topTrailing)
    Box(contentAlignment = Alignment.TopEnd) {

        // Main Bell Button
        IconButton(
            onClick = { showList = !showList },
            modifier = Modifier
                .size(45.dp)
                .background(Color(0xFF333333), CircleShape) // Color(white: 0.2) equivalent
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }

        // Notification Badge (Red Circle)
        if (notifications.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .offset(x = 4.dp, y = (-4).dp)
                    .size(18.dp)
                    .background(Color.Red, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${notifications.size}",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Dropdown List with Animation
        // .transition equivalent using AnimatedVisibility
        AnimatedVisibility(
            visible = showList,
            enter = fadeIn() + scaleIn(
                initialScale = 0.1f,
                transformOrigin = TransformOrigin(1f, 0f) // topTrailing anchor
            ),
            exit = fadeOut() + scaleOut(
                targetScale = 0.1f,
                transformOrigin = TransformOrigin(1f, 0f)
            ),
            modifier = Modifier
                .padding(top = 55.dp)
                .zIndex(1f) // Ensure it floats above other content
        ) {
            NotificationDropdown(
                notifications = notifications,
                onClear = { onNotificationUpdate(emptyList()) }
            )
        }
    }
}

// Simple placeholder for your dropdown component
@Composable
fun NotificationDropdown(notifications: List<String>, onClear: () -> Unit) {
    Surface(
        color = Color(0xFF222222),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
        shadowElevation = 8.dp,
        modifier = Modifier.width(200.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            notifications.forEach { text ->
                Text(text, color = Color.White, fontSize = 12.sp, modifier = Modifier.padding(vertical = 4.dp))
            }
            if (notifications.isNotEmpty()) {
                TextButton(onClick = onClear) {
                    Text("Clear All", color = Color.Gray, fontSize = 11.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun NotificationButtonPreview() {
    val mockNotifications = remember {
        mutableStateListOf("James liked your photo", "New login from Chrome")
    }

    Box(modifier = Modifier.padding(50.dp)) {
        NotificationButton(
            notifications = mockNotifications,
            onNotificationUpdate = { /* Handle clear */ }
        )
    }
}