package com.getyourplace.Components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HeartButton(
    isLiked: Boolean,
    isAuthenticated: Boolean,
    likedColor: Color = Color.Magenta,
    onLikedChange: (Boolean) -> Unit
) {
    // Only show the button if authenticated (SwiftUI 'if auth.isAuthenticated')
    if (isAuthenticated) {

        // 1. Setup the scale animation (SwiftUI .scaleEffect)
        val scale by animateFloatAsState(
            targetValue = if (isLiked) 1.2f else 1.0f,
            animationSpec = spring(
                dampingRatio = 0.6f, // SwiftUI dampingFraction
                stiffness = Spring.StiffnessLow // Controls the "spring" speed
            ),
            label = "HeartScale"
        )

        IconToggleButton(
            checked = isLiked,
            onCheckedChange = { onLikedChange(it) }
        ) {
            Icon(
                imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Like Button",
                tint = if (isLiked) likedColor else Color.Gray,
                modifier = Modifier
                    .size(30.dp)
                    .scale(scale) // Applying the spring animation
            )
        }
    }
}

// --- PREVIEW ---

@Preview(showBackground = true, backgroundColor = 0xFF1A1A1A)
@Composable
fun HeartButtonPreview() {
    // Wrapper to hold state for the preview
    var liked by remember { mutableStateOf(false) }

    MaterialTheme {
        HeartButton(
            isLiked = liked,
            isAuthenticated = true,
            likedColor = Color.Red,
            onLikedChange = { liked = it }
        )
    }
}