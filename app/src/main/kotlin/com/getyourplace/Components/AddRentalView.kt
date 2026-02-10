package com.getyourplace.Components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.getyourplace.Models.RentalHistory
import com.getyourplace.Models.RentalStatus
import java.util.Date
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRentalView(
    onDismiss: () -> Unit,
    onSave: (RentalHistory) -> Unit
) {
    // Equivalent to @State
    var propertyName by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var startDate by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var endDate by remember { mutableLongStateOf(System.currentTimeMillis()) }

    // Control visibility for Android Date Pickers
    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }

    // Apply Dark Theme styling (equivalent to .preferredColorScheme(.dark))
    MaterialTheme(colorScheme = darkColorScheme()) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("New Rental", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        TextButton(onClick = onDismiss) { Text("Cancel", color = Color.White) }
                    },
                    actions = {
                        TextButton(
                            onClick = {
                                val new = RentalHistory(
                                    id = UUID.randomUUID(),
                                    propertyName = propertyName,
                                    location = location,
                                    startDate = Date(startDate),
                                    endDate = Date(endDate),
                                    status = RentalStatus.UPCOMING
                                )
                                onSave(new)
                                onDismiss()
                            },
                            enabled = propertyName.isNotEmpty() && location.isNotEmpty()
                        ) {
                            Text("Save", fontWeight = FontWeight.Bold)
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Section: Property Details
                SectionLabel("Property Details")
                OutlinedTextField(
                    value = propertyName,
                    onValueChange = { propertyName = it },
                    label = { Text("Property Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Section: Dates
                SectionLabel("Dates")
                DateRow("Start Date", Date(startDate).toString()) { showStartPicker = true }
                DateRow("End Date", Date(endDate).toString()) { showEndPicker = true }
            }
        }
    }

    // Modal Date Picker Logic
    if (showStartPicker) {
        MyDatePickerDialog(onDateSelected = { startDate = it }, onDismiss = { showStartPicker = false })
    }
    if (showEndPicker) {
        MyDatePickerDialog(onDateSelected = { endDate = it }, onDismiss = { showEndPicker = false })
    }
}

@Composable
fun SectionLabel(text: String) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        color = Color.Gray,
        modifier = Modifier.padding(bottom = 4.dp)
    )
}

@Composable
fun DateRow(label: String, dateString: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label)
        TextButton(onClick = onClick) {
            Text(dateString)
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun AddRentalViewPreview() {
    // We wrap it in our theme to ensure the Dark Mode displays correctly in the preview
    MaterialTheme(colorScheme = darkColorScheme()) {
        AddRentalView(
            onDismiss = { /* Preview doesn't need to dismiss */ },
            onSave = { rental ->
                println("Saved rental: ${rental.propertyName}")
            }
        )
    }
}