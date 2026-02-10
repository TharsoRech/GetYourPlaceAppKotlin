package com.getyourplace.Components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.getyourplace.Models.InterestedProfile
import com.getyourplace.Models.EngagementStatus
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun ProfileCardView(
    profile: InterestedProfile,
    onAction: (EngagementStatus) -> Unit
) {
    val scope = rememberCoroutineScope()

    // Animation states for X and Y offsets
    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }

    // Logic to calculate rotation based on drag distance
    val rotation = (offsetX.value / 40)

    Box(
        modifier = Modifier
            .offset { IntOffset(offsetX.value.roundToInt(), (offsetY.value * 0.4f).roundToInt()) }
            .rotate(rotation)
            .size(width = 350.dp, height = 500.dp)
            .shadow(10.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color.Gray.copy(alpha = 0.3f), Color.Black)
                )
            )
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        scope.launch {
                            if (offsetX.value > 400f) {
                                // Swiped Right (Accepted)
                                offsetX.animateTo(1000f, tween(300))
                                onAction(EngagementStatus.ACCEPTED)
                            } else if (offsetX.value < -400f) {
                                // Swiped Left (Rejected)
                                offsetX.animateTo(-1000f, tween(300))
                                onAction(EngagementStatus.REJECTED)
                            } else {
                                // Snap back to center
                                launch { offsetX.animateTo(0f, spring()) }
                                launch { offsetY.animateTo(0f, spring()) }
                            }
                        }
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        scope.launch {
                            offsetX.snapTo(offsetX.value + dragAmount.x)
                            offsetY.snapTo(offsetY.value + dragAmount.y)
                        }
                    }
                )
            },
        contentAlignment = Alignment.BottomStart
    ) {
        // Large Background Letter
        Text(
            text = profile.name.take(1),
            modifier = Modifier.align(Alignment.Center),
            fontSize = 80.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White.copy(alpha = 0.2f)
        )

        // Bottom Info Overlay
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                    )
                )
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = profile.name,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Interested in: ${profile.residenceName}",
                    fontSize = 14.sp,
                    color = Color.White
                )
            }

            Button(
                onClick = { onAction(EngagementStatus.SEE_PROFILE) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A1A1A))
            ) {
                Text("See Profile", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// --- Preview ---

@Preview(name = "Interactive Card", showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun ProfileCardPreview() {
    // 1. Setup local state for the preview (equivalent to SwiftUI @State)
    var statusText by remember { mutableStateOf("Status: PENDING") }

    // We use a key here to "restart" the component when we want to reset the card
    var key by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 2. The Card itself
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            key(key) { // This force-restarts the ProfileCardView when key changes
                ProfileCardView(
                    profile = InterestedProfile(
                        name = "Alex Johnson",
                        residenceName = "Sunset Villa",
                        imageUrl = ""
                    ),
                    onAction = { action ->
                        statusText = "Action triggered: ${action.name}"
                    }
                )
            }
        }

        // 3. UI to show current status and a Reset button
        Column(
            modifier = Modifier.padding(bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = statusText,
                color = Color.Gray,
                fontSize = 14.sp
            )

            Button(
                onClick = {
                    key++ // Incrementing the key resets the internal Animatable offsets
                    statusText = "Status: PENDING"
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
            ) {
                Text("Reset Card", color = Color.White)
            }
        }
    }
}