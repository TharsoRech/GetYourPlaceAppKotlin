package com.getyourplace.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 1. The Triangle Shape (SwiftUI Triangle Shape)
val TriangleShape = GenericShape { size, _ ->
    moveTo(size.width / 2f, 0f)
    lineTo(size.width, size.height)
    lineTo(0f, size.height)
    close()
}

@Composable
fun NotificationDropdown(
    notifications: List<String>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.width(280.dp),
        horizontalAlignment = Alignment.End
    ) {
        // 2. The Arrow
        Box(
            modifier = Modifier
                .padding(end = 20.dp) // padding names ARE the same
                .size(width = 20.dp, height = 10.dp)
                .background(Color(0xFFF2F2F2), TriangleShape)
        )

        // 3. The Main Box
        Column(
            modifier = Modifier
                .shadow(elevation = 10.dp, shape = RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF2F2F2)) // SecondarySystemBackground equivalent
                .fillMaxWidth()
        ) {
            Text(
                text = "Notifications",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(16.dp)
            )

            if (notifications.isEmpty()) {
                Text(
                    text = "No new notifications",
                    color = Color.Gray,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                notifications.forEachIndexed { index, note ->
                    NotificationItem(note = note)

                    if (index != notifications.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            thickness = 0.5.dp,
                            color = Color.Black.copy(alpha = 0.1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationItem(note: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Tapped Logic */ }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Bullet Point
        Box(
            modifier = Modifier
                .padding(top = 5.dp)
                .size(8.dp)
                .background(Color(0xFF1A1A1A), CircleShape)
        )

        Text(
            text = note,
            fontSize = 14.sp,
            color = Color.Black,
            lineHeight = 20.sp,
            modifier = Modifier.weight(1f)
        )
    }
}

// --- PREVIEW ---

@Preview(showBackground = false)
@Composable
fun NotificationDropdownPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray.copy(alpha = 0.2f))
            .padding(20.dp),
        contentAlignment = Alignment.TopEnd
    ) {
        NotificationDropdown(
            notifications = listOf(
                "James liked your photo",
                "New login from Chrome",
                "Your subscription was renewed"
            )
        )
    }
}