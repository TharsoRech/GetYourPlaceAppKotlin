package com.getyourplace.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

@Composable
fun StatusBadge(status: String) {
    // Determine the base color based on status
    val statusColor = when (status.lowercase(Locale.ROOT)) {
        "approved" -> Color.Green
        "pending" -> Color(0xFFFFA500) // Orange
        "declined" -> Color.Red
        else -> Color.Blue
    }

    Text(
        text = status.uppercase(Locale.ROOT),
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        color = statusColor,
        modifier = Modifier
            // 1. Background with opacity (SwiftUI .background(Color.opacity(0.2)))
            .clip(CircleShape) // SwiftUI Capsule()
            .background(statusColor.copy(alpha = 0.2f))
            // 2. Border/Stroke (SwiftUI .overlay(Capsule().stroke...))
            .border(1.dp, statusColor.copy(alpha = 0.5f), CircleShape)
            // 3. Padding (SwiftUI .padding)
            .padding(vertical = 4.dp, horizontal = 8.dp)
    )
}

// --- PREVIEWS ---

@Preview(showBackground = true, name = "Approved Status")
@Composable
fun ApprovedBadgePreview() {
    StatusBadge(status = "Approved")
}

@Preview(showBackground = true, name = "Pending Status")
@Composable
fun PendingBadgePreview() {
    StatusBadge(status = "Pending")
}

@Preview(showBackground = true, name = "Declined Status")
@Composable
fun DeclinedBadgePreview() {
    StatusBadge(status = "Declined")
}