package com.getyourplace.Components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Extension on Modifier to apply the shimmering effect.
 * Equivalent to SwiftUI .shimmering()
 */
fun Modifier.shimmering(): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerTranslation"
    )

    val shimmerColors = listOf(
        Color.White.copy(alpha = 0.0f),
        Color.White.copy(alpha = 0.2f),
        Color.White.copy(alpha = 0.0f),
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )

    this.background(brush)
}

// --- Preview ---

@Preview(showSystemUi = true)
@Composable
fun SkeletonShimmerPreview() {
    val skeletonColor = Color.Gray.copy(alpha = 0.3f)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // 1. Circle Skeleton (Profile)
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(skeletonColor, CircleShape)
                    .shimmering()
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .size(width = 150.dp, height = 20.dp)
                        .background(skeletonColor, RoundedCornerShape(4.dp))
                        .shimmering()
                )
                Box(
                    modifier = Modifier
                        .size(width = 100.dp, height = 15.dp)
                        .background(skeletonColor, RoundedCornerShape(4.dp))
                        .shimmering()
                )
            }
        }

        // 2. Large Card Skeleton
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(skeletonColor, RoundedCornerShape(15.dp))
                    .shimmering()
            )
            Box(
                modifier = Modifier
                    .size(width = 200.dp, height = 25.dp)
                    .background(skeletonColor, RoundedCornerShape(4.dp))
                    .shimmering()
            )
        }
    }
}