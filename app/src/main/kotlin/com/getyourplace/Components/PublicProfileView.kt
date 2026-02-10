package com.getyourplace.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.getyourplace.Components.ViewModels.PublicProfileViewModel
import com.getyourplace.Models.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublicProfileView(
    onDismiss: () -> Unit,
    viewModel: PublicProfileViewModel
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val profile by viewModel.profile.collectAsState()
    val rentals by viewModel.rentals.collectAsState()
    val reviews by viewModel.reviews.collectAsState()

    var showReviewSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Public Profile", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                actions = {
                    TextButton(onClick = onDismiss) {
                        Text("Done", color = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF1A1A1A)
                )
            )
        },
        containerColor = Color(0xFF1A1A1A)
    ) { paddingValues ->
        if (isLoading && profile == null) {
            ProfileSkeleton(modifier = Modifier.padding(paddingValues))
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(25.dp),
                contentPadding = PaddingValues(top = 25.dp, bottom = 25.dp)
            ) {
                // Header
                item { HeaderSection(profile) }

                // About
                item { AboutSection(profile) }

                // Rental History
                item { SectionHeader("Rental History") }
                if (rentals.isEmpty()) {
                    item { EmptyStateText("No previous rentals") }
                } else {
                    items(rentals) { rental -> RentalHistoryCard(rental) }
                }

                // Reviews
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SectionHeader("Reviews")
                        TextButton(onClick = { showReviewSheet = true }) {
                            Text("Write a Review", color = Color(0xFF64B5F6), fontSize = 12.sp)
                        }
                    }
                }
                if (reviews.isEmpty()) {
                    item { EmptyStateText("No reviews yet") }
                } else {
                    items(reviews) { review -> ReviewCard(review) }
                }
            }
        }

        if (showReviewSheet) {
            ModalBottomSheet(
                onDismissRequest = { showReviewSheet = false },
                containerColor = Color(0xFF1A1A1A)
            ) {
                // AddReviewPopup placeholder
                Box(Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                    Text("Review Popup Content", color = Color.White)
                }
            }
        }
    }
}

// --- Sub-Components ---

@Composable
private fun HeaderSection(profile: UserProfile?) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .border(2.dp, Color.White.copy(alpha = 0.2f), CircleShape),
            tint = Color.Gray
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(profile?.name ?: "User", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
            profile?.profession?.let {
                Text(it, color = Color(0xFF64B5F6), fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, null, Modifier.size(14.dp), tint = Color.Gray)
                Text(profile?.country ?: "Location not set", color = Color.Gray, fontSize = 14.sp)
            }
        }
    }
}

@Composable
private fun AboutSection(profile: UserProfile?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("About", color = Color.White, fontWeight = FontWeight.Bold)
        Text(profile?.bio ?: "No bio available.", color = Color.Gray, fontSize = 14.sp)
    }
}

@Composable
private fun RentalHistoryCard(rental: RentalHistory) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.03f), RoundedCornerShape(8.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(rental.propertyName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(rental.dateRangeString, color = Color.Gray, fontSize = 12.sp)
        }
        Text(
            text = rental.status.label, // Changed from rawValue to value
            color = Color.White,
            fontSize = 10.sp,
            modifier = Modifier
                .background(Color.Blue.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun ReviewCard(review: UserReview) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(10.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Star, null, Modifier.size(16.dp), tint = Color.Yellow)
            Text("%.1f".format(review.rating), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.padding(start = 4.dp))
            Spacer(Modifier.weight(1f))
            Text(review.relativeDate, color = Color.Gray, fontSize = 12.sp)
        }
        Text(review.comment, color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp)
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
}

@Composable
private fun EmptyStateText(text: String) {
    Text(text, color = Color.Gray, fontSize = 12.sp)
}

@Composable
private fun ProfileSkeleton(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(25.dp)) {
        Box(Modifier.size(100.dp).background(Color.White.copy(0.1f), CircleShape).shimmering())
        Box(Modifier.size(150.dp, 24.dp).background(Color.White.copy(0.1f), RoundedCornerShape(4.dp)).shimmering())
        Box(Modifier.fillMaxWidth().height(100.dp).background(Color.White.copy(0.1f), RoundedCornerShape(12.dp)).shimmering())
    }
}

// --- PREVIEW ---

@Preview(showBackground = true, backgroundColor = 0xFF1A1A1A)
@Composable
fun PublicProfilePreview() {
    // A simple mock for preview purposes
    val mockViewModel = PublicProfileViewModel()

    MaterialTheme {
        PublicProfileView(
            onDismiss = {},
            viewModel = mockViewModel
        )
    }
}