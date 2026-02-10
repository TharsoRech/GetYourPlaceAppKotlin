package com.getyourplace.Components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage // Requires Coil library

@Composable
fun ProfileImageHeader() {
    // 1. State for the selected image URI (SwiftUI @State profileImage)
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // 2. Photo Picker Launcher (SwiftUI PhotosPicker)
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImageUri = uri }
    )

    Box(
        modifier = Modifier
            .padding(top = 10.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        // ZStack(alignment: .bottomTrailing) equivalent
        Box(
            modifier = Modifier.size(160.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            // Main Circle Container
            Surface(
                modifier = Modifier
                    .size(160.dp)
                    .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)), CircleShape),
                shape = CircleShape,
                color = Color.Transparent
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (selectedImageUri != null) {
                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = "Profile Image",
                            modifier = Modifier
                                .size(140.dp)
                                .clip(CircleShape)
                                .clickable {
                                    photoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = null,
                            modifier = Modifier
                                .size(140.dp)
                                .clip(CircleShape)
                                .clickable {
                                    photoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                            tint = Color.Gray.copy(alpha = 0.6f)
                        )
                    }
                }
            }

            // Camera Icon Overlay
            Box(
                modifier = Modifier
                    .offset(x = (-8).dp, y = (-5).dp)
                    .size(38.dp)
                    .background(Color.Gray.copy(alpha = 0.9f), CircleShape)
                    .border(BorderStroke(2.dp, Color(0xFF1A1A1A)), CircleShape), // Cutout effect
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Change Photo",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

// --- PREVIEW ---

@Preview(showBackground = true, name = "Profile Header Preview")
@Composable
fun ProfileImageHeaderPreview() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1A1A1A)) // Dark background to match your theme
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        ProfileImageHeader()
    }
}