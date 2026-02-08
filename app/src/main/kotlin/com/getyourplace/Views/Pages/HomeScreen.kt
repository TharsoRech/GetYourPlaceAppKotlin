package com.getyourplace.Views.Pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import com.getyourplace.R
import com.getyourplace.Components.ArrowButton // Assuming you'll create this component
import com.getyourplace.Persistence.ItemRepository

@Composable
fun HomeScreen(
    itemRepository: ItemRepository
) {
    // Equivalent to NavigationStack + ZStack
    Box(modifier = Modifier.fillMaxSize()) {

        // Background Image (dream_home)
        Image(
            painter = painterResource(id = R.drawable.dream_home),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Equivalent to scaledToFill
        )

        // Main Content Overlay (VStack)
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Branding Section (VStack with spacing 12)
            Column(
                modifier = Modifier.padding(top = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.get_your_place_icon),
                    contentDescription = "Logo",
                    modifier = Modifier.size(50.dp),
                    contentScale = ContentScale.FillBounds
                )

                Text(
                    text = "Get Your Place",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.weight(1f)) // Fills middle space

            // Description Box
            Box(
                modifier = Modifier
                    .padding(horizontal = 40.dp) // Adjusted from 70.dp for better Android fit
                    .background(
                        color = Color.Black.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(15.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = "Your Real Estate Partner Anytime, Anywhere. Find the Perfect Place That Fits your Lifestyle.",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ArrowButton Component
            ArrowButton(
                title = "Find Your Own Place",
                onClick = {  },
                modifier = Modifier
                    .padding(horizontal = 40.dp)
                    .padding(bottom = 30.dp)
            )
        }
    }
}