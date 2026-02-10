package com.getyourplace.Components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.getyourplace.Models.ResidenceFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterView(
    initialFilter: ResidenceFilter,
    onDismiss: () -> Unit,
    onApplyChanges: (ResidenceFilter, Boolean) -> Unit
) {
    // 1. Create a local draft copy of the state
    var draftFilter by remember { mutableStateOf(initialFilter.copy()) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Filters", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    TextButton(onClick = {
                        // Reset to defaults logic
                        val resetFilter = initialFilter.copy(
                            maxPrice = 10000.0,
                            maxSquareFootage = 10000.0,
                            citySelected = "",
                            selections = mutableMapOf()
                        )
                        onApplyChanges(resetFilter, false)
                        onDismiss()
                    }) {
                        Text("Clear", color = Color.Red)
                    }
                },
                actions = {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF1A1A1A)
                )
            )
        },
        containerColor = Color(0xFF1A1A1A)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            // City Picker
            CityPickerView(
                filter = draftFilter,
                onFilterChanged = { updatedFilter -> // Use the correct parameter name here
                    draftFilter = updatedFilter
                }
            )

            // Price Slider
            FilterSlider(
                label = "Price Range (up to $${draftFilter.maxPrice.toInt()})",
                value = draftFilter.maxPrice.toFloat(),
                range = 0f..1000000f, // Adjusted range for realism
                onValueChange = { newValue ->
                    val newSelections = draftFilter.selections.toMutableMap()
                    newSelections["Price"] = "$${newValue.toInt()}"
                    draftFilter = draftFilter.copy(
                        maxPrice = newValue.toDouble(),
                        selections = newSelections
                    )
                }
            )

            // Dynamic Picker Sections (Type, Beds, Baths, etc.)
            DynamicPickerSection(
                filter = draftFilter,
                onFilterChange = { draftFilter = it }
            )

            // Square Footage Slider
            FilterSlider(
                label = "Square Footage (up to ${draftFilter.maxSquareFootage.toInt()}m)",
                value = draftFilter.maxSquareFootage.toFloat(),
                range = 0f..10000f,
                onValueChange = { newValue ->
                    val newSelections = draftFilter.selections.toMutableMap()
                    newSelections["Sqft"] = "${newValue.toInt()}m"
                    draftFilter = draftFilter.copy(
                        maxSquareFootage = newValue.toDouble(),
                        selections = newSelections
                    )
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            // Apply Button
            Button(
                onClick = {
                    onApplyChanges(draftFilter, true)
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text("Apply Filters", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun FilterSlider(
    label: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Normal)
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = Color.White,
                inactiveTrackColor = Color.Gray
            )
        )
    }
}

// --- PREVIEW SECTION ---

@Preview(showBackground = true, backgroundColor = 0xFF1A1A1A)
@Composable
fun FilterViewPreview() {
    // 1. Create a mock filter to show in the preview
    val mockFilter = ResidenceFilter.mock()

    // 2. Wrap in MaterialTheme so the buttons and sliders look correct
    MaterialTheme {
        FilterView(
            initialFilter = mockFilter,
            onDismiss = {
                println("Dismiss clicked")
            },
            onApplyChanges = { updatedFilter, isApplied ->
                println("Applied: $isApplied, New Price: ${updatedFilter.maxPrice}")
            }
        )
    }
}