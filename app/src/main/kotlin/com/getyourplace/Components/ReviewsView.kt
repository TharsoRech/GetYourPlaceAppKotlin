package com.getyourplace.Views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.getyourplace.Components.ViewModels.ReviewsViewModel
import com.getyourplace.Models.UserReview

@Composable
fun ReviewsView(
    viewModel: ReviewsViewModel = viewModel()
) {
    // Trigger fetch on appear
    LaunchedEffect(Unit) {
        viewModel.fetchReviews()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A)) // Dark grey background
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            if (viewModel.isLoading && viewModel.reviews.isEmpty()) {
                items(3) {
                    ReviewSkeletonCard()
                }
            } else if (viewModel.reviews.isEmpty()) {
                item {
                    EmptyStateView()
                }
            } else {
                items(viewModel.reviews) { review ->
                    ReviewCard(review)
                }
            }
        }
    }
}

@Composable
fun ReviewCard(review: UserReview) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color.Yellow,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = String.format("%.1f", review.rating),
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = review.relativeDate,
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
        Text(
            text = review.comment,
            color = Color.White,
            fontSize = 14.sp
        )
    }
}

@Composable
fun ReviewSkeletonCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(20.dp).background(Color.Gray.copy(0.2f), CircleShape))
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.size(40.dp, 15.dp).background(Color.Gray.copy(0.2f), RoundedCornerShape(4.dp)))
            Spacer(modifier = Modifier.weight(1f))
            Box(modifier = Modifier.size(60.dp, 12.dp).background(Color.Gray.copy(0.2f), RoundedCornerShape(4.dp)))
        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(modifier = Modifier.fillMaxWidth().height(14.dp).background(Color.Gray.copy(0.2f), RoundedCornerShape(4.dp)))
            Box(modifier = Modifier.width(200.dp).height(14.dp).background(Color.Gray.copy(0.2f), RoundedCornerShape(4.dp)))
        }
    }
}

@Composable
fun EmptyStateView() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp),
        contentAlignment = Alignment.Center
    ) {
        Text("No reviews yet", color = Color.Gray)
    }
}