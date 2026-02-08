package com.getyourplace.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.getyourplace.Models.UserReview
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReviewPopup(
    onDismiss: () -> Unit,
    onSave: (UserReview) -> Unit
) {
    var comment by remember { mutableStateOf("") }
    var rating by remember { mutableIntStateOf(5) }

    // Using a dark theme matching your SwiftUI Color(red: 0.15...)
    val backgroundColor = Color(0xFF262626)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("New Review", color = Color.White, fontWeight = FontWeight.Bold) },
                colors = topAppBarColors(
        containerColor = backgroundColor
        ),
                navigationIcon = {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = Color.White)
                    }
                }
            )
        },
        containerColor = backgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(25.dp)
        ) {

            // Star Picker (HStack)
            Row(
                modifier = Modifier.padding(top = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (index in 1..5) {
                    Icon(
                        imageVector = if (index <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                        contentDescription = null,
                        tint = Color.Yellow,
                        modifier = Modifier
                            .size(40.dp)
                            .clickable { rating = index }
                    )
                }
            }

            // Text Editor (ZStack style)
            TextField(
                value = comment,
                onValueChange = { comment = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                placeholder = { Text("How was your experience?", color = Color.Gray) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp)
            )

            // Submit Button
            Button(
                onClick = {
                    val newReview = UserReview(
                        id = UUID.randomUUID(),
                        rating = rating.toDouble(),
                        comment = comment,
                        date = Date(),
                        reviewerName = "Guest"
                    )
                    onSave(newReview)
                    onDismiss()
                },
                enabled = comment.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (comment.isEmpty()) Color.Gray else Color.White,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Submit Review", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}