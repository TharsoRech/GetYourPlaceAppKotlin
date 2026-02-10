package com.getyourplace.Components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.getyourplace.Models.ResidenceFilter
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import kotlin.collections.set

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityPickerView(
    filter: ResidenceFilter,
    onFilterChanged: (ResidenceFilter) -> Unit
) {
    var searchText by remember { mutableStateOf(filter.citySelected) }
    var showSuggestions by remember { mutableStateOf(true) }
    val focusManager = LocalFocusManager.current

    // Computed property: Equivalent to filteredCities in SwiftUI
    val filteredCities = remember(searchText, showSuggestions) {
        if (searchText.isEmpty() || !showSuggestions) {
            emptyList()
        } else {
            filter.cities
                .filter { it.contains(searchText, ignoreCase = true) }
                .distinct()
                .sorted()
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "City",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // The Box allows the suggestion list to float on top of other content
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { newValue ->
                    searchText = newValue
                    showSuggestions = true

                    // Update the filter object
                    filter.citySelected = newValue
                    filter.selections["city"] = newValue
                    onFilterChanged(filter)
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search City...", color = Color.Gray) },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )

            // Suggestion List (Equivalent to the .overlay in SwiftUI)
            if (filteredCities.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .padding(top = 60.dp) // Offset below the TextField
                        .fillMaxWidth()
                        .heightIn(max = 200.dp)
                        .zIndex(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.DarkGray.copy(alpha = 0.95f))
                ) {
                    LazyColumn {
                        items(filteredCities) { city ->
                            Column {
                                Text(
                                    text = city,
                                    color = Color.White,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            searchText = city
                                            filter.citySelected = city
                                            filter.selections["city"] = city
                                            showSuggestions = false
                                            onFilterChanged(filter)
                                            focusManager.clearFocus() // Resigns first responder
                                        }
                                        .padding(horizontal = 16.dp, vertical = 12.dp)
                                )
                                HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun CityPickerViewPreview() {
    // 1. Create a mock filter state for the preview
    val mockFilter = remember {
        ResidenceFilter(
            cities = listOf("New York", "Rio de Janeiro", "Budapest", "Atlanta", "London", "Los Angeles", "Lisbon"),
            citySelected = "",
            selections = mutableMapOf()
        )
    }

    // 2. Wrap in a box with padding to see the "Floating" effect of the suggestion list
    Box(modifier = Modifier.padding(20.dp).fillMaxSize()) {
        CityPickerView(
            filter = mockFilter,
            onFilterChanged = { updatedFilter ->
                // In a real app, this would update your ViewModel
                println("City selected: ${updatedFilter.citySelected}")
            }
        )
    }
}