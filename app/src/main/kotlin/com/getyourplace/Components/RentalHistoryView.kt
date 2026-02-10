package com.getyourplace.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.House
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.getyourplace.Components.ViewModels.RentalHistoryViewModel
import com.getyourplace.Models.RentalHistory
import com.getyourplace.Models.RentalStatus
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.Date

@Composable
fun RentalHistoryView(
    // Rename the parameter to avoid conflict with the function viewModel()
    rentalViewModel: RentalHistoryViewModel = viewModel()
) {
    RentalHistoryContent(
        isLoading = rentalViewModel.isLoading,
        rentals = rentalViewModel.rentals,
        onFetch = { rentalViewModel.fetchRentals() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RentalHistoryContent(
    isLoading: Boolean,
    rentals: List<RentalHistory>,
    onFetch: () -> Unit
) {
    var isShowingAddPopup by remember { mutableStateOf(false) }

    // OnAppear equivalent: Triggers when the UI is first drawn
    LaunchedEffect(Unit) {
        onFetch()
    }

    Scaffold(
        containerColor = Color(0xFF1A1A1A),
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = { isShowingAddPopup = true },
                containerColor = Color.Black,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.padding(bottom = 32.dp, end = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (isLoading && rentals.isEmpty()) {
                LazyColumn {
                    items(5) { RentalSkeletonRow() }
                }
            } else if (rentals.isEmpty()) {
                EmptyStateView()
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(rentals) { rental ->
                        RentalRow(rental)
                    }
                }
            }
        }
    }
}

@Composable
fun RentalRow(rental: RentalHistory) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${rental.propertyName} - ${rental.location}",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = rental.dateRangeString,
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
        // Assuming you have the StatusBadge component created in the same package
        StatusBadge(status = rental.status.label)
    }
}

@Composable
fun RentalSkeletonRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .size(180.dp, 18.dp)
                    .background(Color.Gray.copy(0.2f), RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .size(120.dp, 14.dp)
                    .background(Color.Gray.copy(0.2f), RoundedCornerShape(4.dp))
            )
        }
        Box(
            modifier = Modifier
                .size(70.dp, 24.dp)
                .background(Color.Gray.copy(0.2f), RoundedCornerShape(5.dp))
        )
    }
}

@Composable
fun EmptyStateView() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.House,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "No rentals found",
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
    }
}

// --- PREVIEWS ---



@Preview(showBackground = true)
@Composable
fun RentalHistoryPreview() {
    // We simulate the data without needing a real ViewModel instance
    val mockRentals = listOf(
        RentalHistory(
            propertyName = "Modern Villa",
            location = "Los Angeles",
            startDate = Date(),
            endDate = Date(),
            status = RentalStatus.COMPLETED
        ),
        RentalHistory(
            propertyName = "Ocean Studio",
            location = "Miami",
            startDate = Date(),
            endDate = Date(),
            status = RentalStatus.UPCOMING
        )
    )

    MaterialTheme {
        // CALL THE CONTENT VIEW DIRECTLY FOR PREVIEWS
        RentalHistoryContent(
            isLoading = false,
            rentals = mockRentals,
            onFetch = { /* No-op in preview */ }
        )
    }
}

@Preview(showBackground = true, name = "Loading State")
@Composable
fun RentalHistoryLoadingPreview() {
    MaterialTheme {
        RentalHistoryContent(
            isLoading = true,
            rentals = emptyList(),
            onFetch = {}
        )
    }
}