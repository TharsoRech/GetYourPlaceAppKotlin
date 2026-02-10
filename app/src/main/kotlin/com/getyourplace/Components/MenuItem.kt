package com.getyourplace.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * A custom navigation menu item designed to be used inside a Row.
 * Using RowScope allows the use of Modifier.weight(1f) for equal width distribution.
 */
@Composable
fun RowScope.MenuItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    action: () -> Unit
) {
    val contentColor = if (isSelected) Color.White else Color.Black
    val backgroundColor = if (isSelected) Color.Black else Color.White.copy(alpha = 0.9f)

    Box(
        modifier = Modifier
            .weight(1f) // Distributes space equally among all MenuItems in the Row
            .padding(horizontal = 4.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(backgroundColor)
            .clickable { action() }
            .padding(vertical = 8.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(20.dp),
                tint = contentColor
            )
            Text(
                text = label,
                color = contentColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}

// --- PREVIEW SECTION ---

@Preview(showBackground = true, name = "Full Menu Bar Preview")
@Composable
fun FullMenuBarPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray.copy(alpha = 0.3f))
                .padding(vertical = 20.dp)
        ) {
            // The Row provides the 'RowScope' needed by the MenuItems
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                MenuItem(
                    icon = Icons.Default.Home,
                    label = "Home",
                    isSelected = true,
                    action = { /* Navigate Home */ }
                )
                MenuItem(
                    icon = Icons.Default.Key,
                    label = "My Rents",
                    isSelected = false,
                    action = { /* Navigate Rents */ }
                )
                MenuItem(
                    icon = Icons.Default.Favorite,
                    label = "Saved",
                    isSelected = false,
                    action = { /* Navigate Saved */ }
                )
                MenuItem(
                    icon = Icons.Default.ChatBubble,
                    label = "Chat",
                    isSelected = false,
                    action = { /* Navigate Chat */ }
                )
                MenuItem(
                    icon = Icons.Default.AccountCircle,
                    label = "Profile",
                    isSelected = false,
                    action = { /* Navigate Profile */ }
                )
            }
        }
    }
}