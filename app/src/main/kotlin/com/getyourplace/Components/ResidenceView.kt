package com.getyourplace.Components

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.getyourplace.Models.Residence

@Composable
fun ResidenceView(
    residence: Residence,
    onTap: () -> Unit,
    onFavoriteToggle: (Boolean) -> Unit = {}
) {
    // Card Container
    Card(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .fillMaxWidth()
            .shadow(elevation = 12.dp, shape = RoundedCornerShape(24.dp), ambientColor = Color.Black.copy(0.08f))
            .clickable { onTap() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            // Top Section: Image & Badge
            Box(modifier = Modifier.height(250.dp).fillMaxWidth()) {
                // Image decoding logic
                val imageBitmap = remember(residence.mainImageBase64) {
                    try {
                        val decodedBytes = Base64.decode(residence.mainImageBase64, Base64.DEFAULT)
                        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size).asImageBitmap()
                    } catch (e: Exception) {
                        null
                    }
                }

                if (imageBitmap != null) {
                    Image(
                        bitmap = imageBitmap,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize().background(Color.LightGray))
                }

                // Overlay UI (Type Badge and Heart)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    // Type Capsule
                    Surface(
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.9f)
                    ) {
                        Text(
                            text = residence.type,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )
                        )
                    }

                    // Heart Button (Placeholder for your HeartButton component)
                    IconButton(
                        onClick = { onFavoriteToggle(!residence.favorite) },
                        modifier = Modifier.background(Color.White.copy(alpha = 0.3f), CircleShape)
                    ) {
                        // Logic for Red/White heart would go here
                    }
                }
            }

            // Info Section
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    // Title and Location
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = residence.name,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = residence.formattedLocation,
                            fontSize = 15.sp,
                            color = Color.Gray
                        )
                    }

                    // Price and Action
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = residence.formattedPrice,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            tint = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Characteristics Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    ResidenceCharacteristic(residence.formattedNumberOfBeds, "Bed")
                    ResidenceCharacteristic(residence.formattedNumberOfBaths, "Bath")
                    ResidenceCharacteristic(residence.formattedNumberOfGarages, "Garage")
                }
            }
        }
    }
}

@Composable
fun ResidenceCharacteristic(text: String, label: String) {
    // Simplified version of your characteristic component
    Row(modifier = Modifier.padding(end = 16.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(text = text, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1A1A1A)
@Composable
fun ResidenceViewPreview() {
    // Creating a mock residence object
    val context = LocalContext.current // Gets the Android Context for the preview
    val mockResidence = Residence.mock(context)

    MaterialTheme {
        // We wrap it in a Box with padding so it doesn't hug the preview edges
        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            ResidenceView(
                residence = mockResidence,
                onTap = { /* Action for preview clicks */ },
                onFavoriteToggle = { isLiked -> println("Liked: $isLiked") }
            )
        }
    }
}