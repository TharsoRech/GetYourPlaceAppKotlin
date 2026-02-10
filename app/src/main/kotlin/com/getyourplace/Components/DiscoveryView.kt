package com.getyourplace.Views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.getyourplace.Components.ProfileCardView
import com.getyourplace.Components.PublicProfileView
import com.getyourplace.Models.InterestedProfile
import com.getyourplace.Models.EngagementStatus
import com.getyourplace.Components.ViewModels.PublicProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoveryView(
    profiles: List<InterestedProfile>,
    onProfileUpdate: (InterestedProfile, EngagementStatus) -> Unit
) {
    // State to control the Bottom Sheet visibility
    var showSheet by remember { mutableStateOf(false) }

    // In a real app, you might pass an ID to the ViewModel to fetch specific data
    val publicProfileViewModel = remember { PublicProfileViewModel() }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Discover", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF1A1A1A)
                )
            )
        },
        containerColor = Color(0xFF1A1A1A)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            val pendingProfiles = profiles.filter { it.status == EngagementStatus.PENDING }

            if (pendingProfiles.isEmpty()) {
                Text("No more profiles", color = Color.Gray)
            } else {
                Box(
                    modifier = Modifier.fillMaxSize().padding(bottom = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Display cards in a stack
                    pendingProfiles.reversed().forEach { profile ->
                        ProfileCardView(
                            profile = profile,
                            onAction = { action ->
                                when (action) {
                                    EngagementStatus.SEE_PROFILE -> {
                                        showSheet = true
                                    }
                                    EngagementStatus.ACCEPTED, EngagementStatus.REJECTED -> {
                                        onProfileUpdate(profile, action)
                                    }
                                    else -> {}
                                }
                            }
                        )
                    }
                }
            }
        }

        // The Bottom Sheet (SwiftUI .sheet equivalent)
        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSheet = false },
                containerColor = Color(0xFF1A1A1A),
                dragHandle = { BottomSheetDefaults.DragHandle(color = Color.Gray) }
            ) {
                // Call the View with the ViewModel it expects
                PublicProfileView(
                    onDismiss = { showSheet = false },
                    viewModel = publicProfileViewModel
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1A1A1A)
@Composable
fun DiscoveryViewPreview() {
    // We create a local state list so the preview can actually handle swiping/removing cards
    val mockProfiles = remember {
        mutableStateListOf(
            InterestedProfile(
                name = "Alex Sterling",
                residenceName = "Sunset Villa",
                imageUrl = "",
                status = EngagementStatus.PENDING
            ),
            InterestedProfile(
                name = "Sarah Jenkins",
                residenceName = "Urban Loft",
                imageUrl = "",
                status = EngagementStatus.PENDING
            ),
            InterestedProfile(
                name = "Michael Chen",
                residenceName = "Beachside Condo",
                imageUrl = "",
                status = EngagementStatus.PENDING
            )
        )
    }

    MaterialTheme {
        DiscoveryView(
            profiles = mockProfiles,
            onProfileUpdate = { profile, newStatus ->
                // This logic allows the preview to react when you swipe
                val index = mockProfiles.indexOf(profile)
                if (index != -1) {
                    mockProfiles[index] = profile.copy(status = newStatus)
                }
            }
        )
    }
}