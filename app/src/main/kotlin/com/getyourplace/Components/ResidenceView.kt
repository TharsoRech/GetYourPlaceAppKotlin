package com.getyourplace.Components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.Bathtub
import androidx.compose.material.icons.filled.Garage
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.getyourplace.Models.Residence

@Composable
fun ResidenceView(
    residence: Residence,
    onTap: () -> Unit,
    onFavoriteToggle: (Boolean) -> Unit = {}
) {
    var isLiked by remember(residence.id, residence.favorite) {
        mutableStateOf(residence.favorite)
    }

    Card(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .fillMaxWidth()
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = Color.Black.copy(0.08f)
            )
            .clickable { onTap() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Box(modifier = Modifier.height(250.dp).fillMaxWidth()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(residence.imageRes ?: residence.mainImageBase64) // It handles both!
                        .crossfade(true)
                        .build(),
                    contentDescription = residence.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
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

                    HeartButton(
                        isLiked = isLiked,
                        isAuthenticated = true,
                        likedColor = Color.Red,
                        onLikedChange = { newValue ->
                            isLiked = newValue
                            onFavoriteToggle(newValue)
                        }
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
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

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp), // Spacing between pills
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Bed characteristic
                    ResidenceCharacteristics(
                        text = residence.formattedNumberOfBeds,
                        icon = Icons.Default.Bed
                    )

                    // Bath characteristic
                    ResidenceCharacteristics(
                        text = residence.formattedNumberOfBaths,
                        icon = Icons.Default.Bathtub // Requires material-icons-extended
                    )

                    // Garage characteristic (Conditional)
                    if (residence.hasGarage) {
                        ResidenceCharacteristics(
                            text = residence.formattedNumberOfGarages,
                            icon = Icons.Default.Garage
                        )
                    }
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResidenceViewPreview() {
    val context = LocalContext.current
    val mockResidence = Residence.mock(context)
    MaterialTheme {
        ResidenceView(residence = mockResidence, onTap = {})
    }
}