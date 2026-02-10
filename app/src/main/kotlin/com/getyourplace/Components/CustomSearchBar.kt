package com.getyourplace.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CustomSearchBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSearchTap: () -> Unit,
    onFilterTap: () -> Unit,
    isFilterActive: Boolean,
    modifier: Modifier = Modifier
) {
    // Outer Container (The dark 15dp corner radius background)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(15.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Search Icon Button
        IconButton(
            onClick = onSearchTap,
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.Gray
            )
        }

        // Inner Text Field
        TextField(
            value = text,
            onValueChange = onTextChange,
            placeholder = {
                Text("Search your home", color = Color.Gray)
            },
            modifier = Modifier
                .weight(1f)
                .heightIn(min = 40.dp)
                .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(10.dp)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            trailingIcon = {
                if (text.isNotEmpty()) {
                    IconButton(onClick = {
                        onTextChange("")
                        onSearchTap()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear",
                            tint = Color.Gray,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearchTap() }),
            singleLine = true,
            shape = RoundedCornerShape(10.dp)
        )

        // Filter Button
        IconButton(onClick = onFilterTap) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = "Filter",
                tint = if (isFilterActive) Color.Cyan else Color.White, // Using Cyan to mimic active fill look
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

// --- Preview ---

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun CustomSearchBarPreview() {
    var searchText by remember { mutableStateOf("Example Text") }
    var filterActive by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp)
    ) {
        CustomSearchBar(
            text = searchText,
            onTextChange = { searchText = it },
            onSearchTap = { /* Handle Search */ },
            onFilterTap = { filterActive = !filterActive },
            isFilterActive = filterActive
        )
    }
}