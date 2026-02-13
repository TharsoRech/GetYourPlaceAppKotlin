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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HeartButton(
    isLiked: Boolean,
    isAuthenticated: Boolean,
    likedColor: Color = Color.Red, // Changed default to Red
    onLikedChange: (Boolean) -> Unit
) {
    if (isAuthenticated) {
        // Setup the scale animation
        val scale by animateFloatAsState(
            targetValue = if (isLiked) 1.3f else 1.0f, // Slightly higher zoom for effect
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ),
            label = "HeartScale"
        )

        IconToggleButton(
            checked = isLiked,
            onCheckedChange = { onLikedChange(it) },
            // Explicitly set colors for the toggle button container
            colors = IconButtonDefaults.iconToggleButtonColors(
                contentColor = Color.Gray,
                checkedContentColor = likedColor
            )
        ) {
            Icon(
                imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Like Button",
                // Use the state to drive the color directly if the button colors fail
                tint = if (isLiked) likedColor else Color.Gray,
                modifier = Modifier
                    .size(30.dp)
                    .scale(scale)
            )
        }
    }
}

// --- PREVIEW ---
@Preview(showBackground = true, backgroundColor = 0xFF1A1A1A)
@Composable
fun HeartButtonPreview() {
    // This state is CRITICAL. In your real app, make sure the
    // ViewModel or parent state is actually updating 'isLiked'.
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