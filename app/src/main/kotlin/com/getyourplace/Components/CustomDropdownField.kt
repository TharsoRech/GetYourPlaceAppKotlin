package com.getyourplace.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomDropdownField(
    label: String,
    value: String,
    options: List<String> = listOf("Nigeria", "United States", "United Kingdom", "Canada"),
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Label
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        // Dropdown Box (The HStack equivalent)
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(10.dp))
                    .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(10.dp))
                    .clickable { expanded = true }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = value,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            // The actual Menu
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(Color(0xFF2C2C2E)) // Dark background for the menu
                    .fillMaxWidth(0.9f)
            ) {
                options.forEach { selection ->
                    DropdownMenuItem(
                        text = { Text(selection, color = Color.White) },
                        onClick = {
                            onOptionSelected(selection)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

// --- Preview ---

@Preview(name = "Country Dropdown")
@Composable
fun CustomDropdownFieldPreview() {
    var country by remember { mutableStateOf("Nigeria") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
            .padding(16.dp)
    ) {
        CustomDropdownField(
            label = "COUNTRY",
            value = country,
            onOptionSelected = { country = it }
        )
    }
}