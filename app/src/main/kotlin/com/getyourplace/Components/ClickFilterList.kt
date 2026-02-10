package com.getyourplace.Components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ClickFilterList(
    filters: List<String>,
    modifier: Modifier = Modifier,
    // Change Void to Unit here
    onClickFilter: (String) -> Unit = {}
) {
    var selectedFilter by remember { mutableStateOf("Newest") }

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(filters) { filter ->
            ClickFilter(
                title = filter,
                isSelected = selectedFilter == filter,
                onClick = {
                    selectedFilter = filter
                    onClickFilter(filter)
                }
            )
        }
    }
}

// --- Preview ---

@Preview(showBackground = true, backgroundColor = 0xFFF0F0F0, name = "Standard List")
@Composable
fun ClickFilterListPreview() {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp)) {
        ClickFilterList(
            filters = listOf("All", "Tech", "Design", "News", "Real Estate", "Luxury")
        )
    }
}

@Preview(showBackground = true, name = "Empty List")
@Composable
fun ClickFilterListEmptyPreview() {
    // Verifying it doesn't crash or look weird when filters is empty
    ClickFilterList(filters = emptyList())
}