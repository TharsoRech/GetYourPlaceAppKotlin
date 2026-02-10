package com.getyourplace.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MatchsResidencesSkeleton() {
    // Matches the base color used in your preview
    val skeletonColor = Color.Gray.copy(alpha = 0.3f)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Profile Header Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(skeletonColor, CircleShape)
                    .shimmering() // Using your custom extension
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
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

        // Main Discovery Content Card
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp) // Adjusted to match your "Discovery look"
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

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1A1A1A)
@Composable
fun MatchsResidencesSkeletonPreview() {
    MatchsResidencesSkeleton()
}