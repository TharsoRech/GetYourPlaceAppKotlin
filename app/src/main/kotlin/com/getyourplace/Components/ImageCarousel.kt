package com.getyourplace.Components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.House
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import coil.compose.AsyncImage

@Composable
fun ImageCarousel(
    imageResIds: List<Int>, // Changed from List<Bitmap> to List<Int>
    modifier: Modifier = Modifier
) {
    if (imageResIds.isEmpty()) {
        PlaceholderView(modifier)
    } else {
        val pagerState = rememberPagerState(pageCount = { imageResIds.size })

        Box(modifier = modifier.fillMaxWidth()) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                // Using AsyncImage instead of Image(bitmap) to prevent UI lag
                AsyncImage(
                    model = imageResIds[page],
                    contentDescription = "Carousel Image $page",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // Indicator Dots
            Row(
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(pagerState.pageCount) { iteration ->
                    val color = if (pagerState.currentPage == iteration) Color.White else Color.White.copy(alpha = 0.5f)
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun PlaceholderView(modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.1f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.House,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.2f),
            modifier = Modifier.size(48.dp)
        )
    }
}

@Preview(name = "Image Carousel")
@Composable
fun ImageCarouselPreview() {
    // Use real drawable resource IDs from your project
    // I'm using house1, house2, house3 based on your Residence mocks
    val mockResourceIds = listOf(
        com.getyourplace.R.drawable.house1,
        com.getyourplace.R.drawable.house2,
        com.getyourplace.R.drawable.house3
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D12)) // Dark background to match your popup
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        ImageCarousel(
            imageResIds = mockResourceIds,
            modifier = Modifier
                .height(250.dp)
                .clip(RoundedCornerShape(20.dp))
        )
    }
}