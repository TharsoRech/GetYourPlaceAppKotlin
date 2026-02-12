package com.getyourplace.Components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.getyourplace.Components.ViewModels.MatchsResidencesViewModel
import com.getyourplace.Managers.AuthManager
import com.getyourplace.Models.Conversation
import com.getyourplace.Models.UserRole
import com.getyourplace.Models.EngagementStatus
import com.getyourplace.Repository.ChatRepository
import com.getyourplace.Repository.MatchsRepository
import com.getyourplace.Views.DiscoveryView
import com.getyourplace.Views.HistoryListView
import kotlinx.coroutines.launch

class MatchsResidencesViewModelFactory(
    private val authManager: AuthManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MatchsResidencesViewModel::class.java)) {
            val chatRepo = ChatRepository()
            val matchsRepo = MatchsRepository()

            @Suppress("UNCHECKED_CAST")
            return MatchsResidencesViewModel(chatRepo, matchsRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@Composable
fun MatchsResidencesView(
    onTabChange: (Int) -> Unit,
    onConversationActive: (Conversation) -> Unit,
    authManager: AuthManager,
    viewModel: MatchsResidencesViewModel = viewModel(
        factory = MatchsResidencesViewModelFactory(authManager)
    )
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()
    val currentUser by authManager.currentUser.collectAsState()

    Scaffold(
        containerColor = Color(0xFF1A1A1A),
        bottomBar = {
            // This Column structure replicates the SwiftUI .padding(.bottom, 100)
            Column(modifier = Modifier.background(Color.Transparent)) {
                NavigationBar(
                    containerColor = Color.Black,
                    tonalElevation = 0.dp,
                    modifier = Modifier.height(80.dp)
                ) {
                    val itemColors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = Color.White,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color.Transparent // Removes the Material "Pill"
                    )

                    NavigationBarItem(
                        selected = selectedTabIndex == 0,
                        onClick = { selectedTabIndex = 0 },
                        icon = { Icon(Icons.Default.Layers, null) },
                        label = { Text("Pending") },
                        colors = itemColors
                    )
                    NavigationBarItem(
                        selected = selectedTabIndex == 1,
                        onClick = { selectedTabIndex = 1 },
                        icon = { Icon(Icons.Default.CheckCircle, null) },
                        label = { Text("Accepted") },
                        colors = itemColors
                    )
                    NavigationBarItem(
                        selected = selectedTabIndex == 2,
                        onClick = { selectedTabIndex = 2 },
                        icon = { Icon(Icons.Default.Cancel, null) },
                        label = { Text("Rejected") },
                        colors = itemColors
                    )
                }
                // The bottom gap from your SwiftUI code
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    ) { padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {
            if (viewModel.isLoading) {
                MatchsResidencesSkeleton()
            } else {
                Crossfade(targetState = selectedTabIndex, label = "TabTransition") { targetIndex ->
                    when (targetIndex) {
                        0 -> {
                            if (currentUser?.role == UserRole.OWNER) {
                                DiscoveryView(
                                    profiles = viewModel.profiles,
                                    onProfileUpdate = { profile, newStatus ->
                                        viewModel.updateProfileStatus(profile, newStatus)
                                    }
                                )
                            } else {
                                HistoryListView(
                                    profiles = viewModel.profiles,
                                    filter = EngagementStatus.PENDING,
                                    onChatTap = {},
                                    onStatusUpdate = { profile, status ->
                                        viewModel.updateProfileStatus(profile, status)
                                    }
                                )
                            }
                        }
                        1 -> {
                            HistoryListView(
                                profiles = viewModel.profiles,
                                filter = EngagementStatus.ACCEPTED,
                                onChatTap = { profile ->
                                    scope.launch {
                                        val conv = viewModel.getConversation(profile)
                                        onConversationActive(conv)
                                        onTabChange(1)
                                    }
                                },
                                onStatusUpdate = { profile, status ->
                                    viewModel.updateProfileStatus(profile, status)
                                }
                            )
                        }
                        2 -> {
                            HistoryListView(
                                profiles = viewModel.profiles,
                                filter = EngagementStatus.REJECTED,
                                onChatTap = {},
                                onStatusUpdate = { profile, status ->
                                    viewModel.updateProfileStatus(profile, status)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

// --- PREVIEWS ---
@Preview(showBackground = true, backgroundColor = 0xFF1A1A1A, name = "Owner View")
@Composable
fun MatchsResidencesViewOwnerPreview() {
    val context = LocalContext.current
    val ownerAuth = remember { AuthManager.mock(context, UserRole.OWNER) }

    MaterialTheme {
        MatchsResidencesView(
            onTabChange = {},
            onConversationActive = {},
            authManager = ownerAuth
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1A1A1A, name = "Renter View")
@Composable
fun MatchsResidencesViewRenterPreview() {
    val context = LocalContext.current
    val renterAuth = remember { AuthManager.mock(context, UserRole.RENTER) }

    MaterialTheme {
        MatchsResidencesView(
            onTabChange = {},
            onConversationActive = {},
            authManager = renterAuth
        )
    }
}