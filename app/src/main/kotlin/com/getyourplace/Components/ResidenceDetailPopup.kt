package com.getyourplace.Components

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.getyourplace.Models.Residence
import com.getyourplace.R
import kotlinx.coroutines.delay

@Composable
fun ResidenceDetailPopup(
    residence: Residence,
    onDismiss: () -> Unit,
    onEditClick: (Residence) -> Unit
) {
    var isLoading by remember { mutableStateOf(true) }
    var showingInterestPopup by remember { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    // Equivalent to loadDataSequentially
    LaunchedEffect(Unit) {
        delay(500)
        isLoading = false
    }

    Dialog(
        onDismissRequest = { if (!showingInterestPopup) onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.8f))
                .clickable(enabled = !showingInterestPopup) { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            // Main Popup Content (The "VStack" from SwiftUI)
            Column(
                modifier = Modifier
                    .width(screenWidth * 0.9f)
                    .height(screenHeight * 0.8f)
                    .clickable(enabled = false) { }
                    .clip(RoundedCornerShape(32.dp))
                    .background(Color(0xFF0D0D12).copy(alpha = 0.95f))
                    .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(32.dp))
            ) {
                ImageCarousel(
                    imageResIds = residence.galleryImageRes.filterNotNull(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp) // Adjusted height for a nicer look
                        .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    TitleAndPriceSection(residence)
                    RatingBadge(residence.rating)
                    HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
                    InfoGrid(residence)
                    HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
                    DescriptionSection(residence.description)
                }

                FooterActions(
                    residence = residence,
                    onContactClick = { showingInterestPopup = true },
                    onEditClick = onEditClick
                )
            }

            // Close Button
            Box(modifier = Modifier
                .width(screenWidth * 0.9f)
                .height(screenHeight * 0.8f),
                contentAlignment = Alignment.TopEnd
            ) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .padding(16.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                        .size(36.dp)
                ) {
                    Icon(Icons.Default.Close, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }

            if (showingInterestPopup) {
                InterestAlertOverlay {
                    showingInterestPopup = false
                    onDismiss()
                }
            }
        }
    }
}

@Composable
fun TitleAndPriceSection(residence: Residence) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
        Column(modifier = Modifier.weight(1f)) {
            Text(residence.name, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                Icon(Icons.Default.LocationOn, null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                Text(residence.location, color = Color.Gray, fontSize = 14.sp)
            }
        }
        Surface(color = Color.White.copy(0.1f), shape = RoundedCornerShape(10.dp)) {
            Text(
                residence.formattedPrice,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun RatingBadge(rating: Double) {
    Surface(
        color = Color.White.copy(alpha = 0.1f),
        shape = RoundedCornerShape(6.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(Icons.Default.Star, null, tint = Color.Yellow, modifier = Modifier.size(14.dp))
            Text(String.format("%.1f", rating), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Text("(Reviews)", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
        }
    }
}

@Composable
fun InfoGrid(residence: Residence) {
    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            InfoItem(Icons.Default.Bed, residence.formattedNumberOfBeds)
            InfoItem(Icons.Default.Shower, residence.formattedNumberOfBaths)
            InfoItem(Icons.Default.SquareFoot, "${residence.squareFootage.toInt()} sqft")
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            InfoItem(Icons.Default.DirectionsCar, residence.formattedNumberOfGarages)
            InfoItem(Icons.Default.MeetingRoom, residence.formattedNumberOfRooms)
            InfoItem(Icons.Default.Pets, residence.petsStatus)
        }
    }
}

@Composable
fun InfoItem(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.widthIn(min = 80.dp)) {
        Icon(icon, null, tint = Color.Gray, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(6.dp))
        Text(text, color = Color.White.copy(0.8f), fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun DescriptionSection(description: String) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("About this property", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text(
            text = description.ifEmpty { "No description provided." },
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp,
            lineHeight = 20.sp
        )
    }
}

@Composable
fun HorizontalDivider(color: Color) {
    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(color))
}

@Composable
fun FooterActions(residence: Residence, onContactClick: () -> Unit, onEditClick: (Residence) -> Unit) {
    Box(modifier = Modifier.padding(horizontal = 24.dp).padding(bottom = 36.dp)) {
        Button(
            onClick = {
                if (residence.isMine) onEditClick(residence) else onContactClick()
            },
            modifier = Modifier.fillMaxWidth().height(55.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                if (residence.isMine) "Edit My Property" else "Contact Person",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun InterestAlertOverlay(onDone: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black.copy(0.4f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .width(320.dp)
                .background(Color(0xFF1E1E26), RoundedCornerShape(30.dp))
                .padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Icon(Icons.Default.Send, null, tint = Color.White, modifier = Modifier.size(40.dp))
            Text("Request Sent", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(
                "A message will be sent to the owner about your interest. They will conduct an evaluation shortly.\n\nTrack your application in the Interests section.",
                color = Color.White.copy(0.7f),
                textAlign = TextAlign.Center,
                fontSize = 14.sp
            )
            Button(
                onClick = onDone,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(15.dp)
            ) {
                Text("Got it", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResidenceDetailPopupPreview() {
    val context = LocalContext.current
    val mock = Residence.mock(context)

    MaterialTheme {
        ResidenceDetailPopup(
            residence = mock,
            onDismiss = {},
            onEditClick = {}
        )
    }
}