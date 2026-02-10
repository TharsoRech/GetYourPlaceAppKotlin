package com.getyourplace.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ResidenceCharacteristics(
    text: String,
    icon: ImageVector, // We pass the ImageVector directly for type safety
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            // Equivalent to height(100) in SwiftUI, though 100dp is quite large for a pill.
            // Using a standard height or wrapContent to match your padding logic.
            .heightIn(max = 100.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .background(
                    color = Color.Gray.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(32.dp) // High corner radius creates the pill shape
                )
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.width(4.dp)) // Added small gap between text and icon

            Icon(
                imageVector = icon,
                contentDescription = null, // Decorative icon
                modifier = Modifier.size(14.dp),
                tint = Color.Black
            )
        }
    }
}

// --- PREVIEW ---

@Preview(showBackground = true)
@Composable
fun ResidenceCharacteristicsPreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        ResidenceCharacteristics(
            text = "3 Bedrooms",
            icon = Icons.Default.Bed
        )
    }
}