package com.getyourplace.Components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ImageCarousel(
    images: List<Bitmap>, // Android uses Bitmap for UIImage equivalent
    modifier: Modifier = Modifier
) {
    if (images.isEmpty()) {
        PlaceholderView(modifier)
    } else {
        // rememberPagerState is the controller for current page and total count
        val pagerState = rememberPagerState(pageCount = { images.size })

        Box(modifier = modifier.fillMaxWidth()) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                Image(
                    bitmap = images[page].asImageBitmap(),
                    contentDescription = "Carousel Image $page",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop // Equivalent to .aspectRatio(contentMode: .fill)
                )
            }

            // Indicator Dots (SwiftUI PageTabViewStyle)
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
    // Creating a simple 1x1 Bitmap for mock data
    val conf = Bitmap.Config.ARGB_8888
    val redBitmap = Bitmap.createBitmap(1, 1, conf).apply { setPixel(0, 0, android.graphics.Color.RED) }
    val blueBitmap = Bitmap.createBitmap(1, 1, conf).apply { setPixel(0, 0, android.graphics.Color.BLUE) }

    val mockImages = listOf(redBitmap, blueBitmap)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        ImageCarousel(
            images = mockImages,
            modifier = Modifier
                .height(250.dp)
                .clip(androidx.compose.foundation.shape.RoundedCornerShape(20.dp))
        )
    }
}