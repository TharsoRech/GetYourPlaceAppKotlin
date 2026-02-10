package com.getyourplace.Views

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke // Fixed
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.getyourplace.Models.InterestedProfile
import com.getyourplace.Models.EngagementStatus

@Composable
fun HistoryListView(
    profiles: List<InterestedProfile>,
    filter: EngagementStatus,
    onChatTap: (InterestedProfile) -> Unit,
    onStatusUpdate: (InterestedProfile, EngagementStatus) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val filteredList = profiles.filter { it.status == filter }

            items(filteredList) { profile ->
                HistoryRow(
                    profile = profile,
                    showChat = filter == EngagementStatus.ACCEPTED,
                    onChatTap = { onChatTap(profile) },
                    onReset = { onStatusUpdate(profile, EngagementStatus.PENDING) }
                )
            }
        }
    }
}

@Composable
fun HistoryRow(
    profile: InterestedProfile,
    showChat: Boolean,
    onChatTap: () -> Unit,
    onReset: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = profile.name, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = profile.residenceName, color = Color.Gray, fontSize = 14.sp)
        }

        if (showChat) {
            IconButton(
                onClick = onChatTap,
                modifier = Modifier
                    .background(Color.Blue.copy(alpha = 0.2f), CircleShape)
                    .size(40.dp)
            ) {
                Icon(Icons.Default.Message, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
        }

        if (profile.status == EngagementStatus.PENDING) {
            Surface(
                color = Color.Blue.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(Icons.Default.PersonSearch, contentDescription = null, tint = Color.Blue, modifier = Modifier.size(16.dp))
                    Text("Analyzing", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
            }
        } else {
            // FIXED: BorderStroke instead of BoxStroke
            OutlinedButton(
                onClick = onReset,
                border = BorderStroke(1.dp, Color.Yellow.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                modifier = Modifier.height(32.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Yellow)
            ) {
                Text("Reset", fontSize = 12.sp)
            }
        }
    }
}

// --- PREVIEW ---

@Preview(showBackground = true, backgroundColor = 0xFF1A1A1A)
@Composable
fun HistoryListViewPreview() {
    // FIXED: Using named arguments to avoid UUID Type Mismatch
    val mockProfiles = listOf(
        InterestedProfile(
            name = "Jordan Lee",
            residenceName = "Beach House",
            imageUrl = "",
            status = EngagementStatus.ACCEPTED
        ),
        InterestedProfile(
            name = "Taylor Reed",
            residenceName = "Mountain Cabin",
            imageUrl = "",
            status = EngagementStatus.ACCEPTED
        )
    )

    MaterialTheme {
        HistoryListView(
            profiles = mockProfiles,
            filter = EngagementStatus.ACCEPTED,
            onChatTap = {},
            onStatusUpdate = { _, _ -> }
        )
    }
}