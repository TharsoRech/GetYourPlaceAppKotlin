package com.getyourplace.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ClickFilter(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    // Determine colors based on selection state
    val backgroundColor = if (isSelected) Color.White else Color.Black.copy(alpha = 0.9f)
    val textColor = if (isSelected) Color.Black else Color.White

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(32.dp)) // Equivalent to CornerRadius
            .background(backgroundColor)
            .clickable { onClick() } // Equivalent to Button action
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Text(
            text = title,
            color = textColor,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ClickFilterPreview() {
    ClickFilter(
        title = "Any Type",
        isSelected = false,
        onClick = {}
    )
}