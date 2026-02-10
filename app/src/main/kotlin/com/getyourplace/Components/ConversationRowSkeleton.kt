package com.getyourplace.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ConversationRowSkeleton(
    modifier: Modifier = Modifier
) {
    val skeletonColor = Color.Gray.copy(alpha = 0.3f)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Profile Picture Circle
        Box(
            modifier = Modifier
                .size(55.dp)
                .background(skeletonColor, CircleShape)
                .shimmering() // Using the custom modifier we built
        )

        Spacer(modifier = Modifier.width(15.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Name Skeleton
            Box(
                modifier = Modifier
                    .size(width = 120.dp, height = 18.dp)
                    .background(skeletonColor, RoundedCornerShape(4.dp))
                    .shimmering()
            )

            // Last Message Snippet Skeleton
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(14.dp)
                    .background(skeletonColor, RoundedCornerShape(4.dp))
                    .shimmering()
            )
        }
    }
}

// --- Preview ---

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun ConversationRowSkeletonPreview() {
    Column(modifier = Modifier.padding(16.dp)) {
        // Show a few rows to see the list effect
        repeat(3) {
            ConversationRowSkeleton()
        }
    }
}