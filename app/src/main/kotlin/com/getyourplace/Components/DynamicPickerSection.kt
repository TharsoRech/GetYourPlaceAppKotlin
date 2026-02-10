package com.getyourplace.Components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.getyourplace.Models.ResidenceFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DynamicPickerSection(
    filter: ResidenceFilter,
    onFilterChange: (ResidenceFilter) -> Unit
) {
    // Sort keys alphabetically to match your SwiftUI logic
    val sortedKeys = filter.pickerOptions.keys.sorted()

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        sortedKeys.forEach { key ->
            val options = filter.pickerOptions[key] ?: emptyList()
            // Get the current selection or default to the first option if none exists
            val selectedOption = filter.selections[key] ?: options.firstOrNull() ?: ""

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = key,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    options.forEachIndexed { index, option ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = options.size
                            ),
                            onClick = {
                                // Update logic: Create a new map to trigger state change
                                val newSelections = filter.selections.toMutableMap()
                                newSelections[key] = option
                                onFilterChange(filter.copy(selections = newSelections))
                            },
                            selected = (option == selectedOption),
                            colors = SegmentedButtonDefaults.colors(
                                activeContainerColor = Color.White,
                                activeContentColor = Color.Black,
                                inactiveContainerColor = Color.Transparent,
                                inactiveContentColor = Color.White,
                                activeBorderColor = Color.White,
                                inactiveBorderColor = Color.White.copy(alpha = 0.5f)
                            )
                        ) {
                            Text(option, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

// --- Preview ---

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun DynamicPickerSectionPreview() {
    // In a real app, this state would live in your ViewModel
    val mockFilter = ResidenceFilter.mock()

    Box(Modifier.padding(20.dp)) {
        DynamicPickerSection(
            filter = mockFilter,
            onFilterChange = { /* Update state here */ }
        )
    }
}