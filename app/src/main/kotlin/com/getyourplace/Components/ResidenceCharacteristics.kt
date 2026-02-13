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
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    // Note: removed Box and fillMaxWidth() to allow horizontal stacking
    Row(
        modifier = modifier
            .background(
                color = Color.Gray.copy(alpha = 0.15f),
                shape = RoundedCornerShape(32.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // Swapped order: Icon then Text usually looks better in real estate apps
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = Color.Black.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )
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