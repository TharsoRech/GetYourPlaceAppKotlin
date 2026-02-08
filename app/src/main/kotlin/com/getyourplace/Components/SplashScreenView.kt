package com.getyourplace.Components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.getyourplace.R

@Composable
fun SplashScreenView() {
    // 1. Animation States (Equivalent to @State scale and opacity)
    var startAnimation by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1.0f else 0.8f,
        animationSpec = tween(durationMillis = 1200),
        label = "scale"
    )

    val opacity by animateFloatAsState(
        targetValue = if (startAnimation) 1.0f else 0.5f,
        animationSpec = tween(durationMillis = 1200),
        label = "opacity"
    )

    // Trigger the animation on appear
    LaunchedEffect(Unit) {
        startAnimation = true
    }

    // 2. UI Layout (Equivalent to ZStack + Color.ignoresSafeArea)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A)), // Dark gray (red: 0.1, green: 0.1, blue: 0.1)
        contentAlignment = Alignment.Center
    ) {
        // Equivalent to VStack
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .scale(scale)
                .alpha(opacity)
        ) {
            Text(
                text = "Welcome To",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(id = R.drawable.get_your_place_icon), // Ensure you add this icon to res/drawable
                contentDescription = "App Icon",
                modifier = Modifier.size(150.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Get Your Place",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}