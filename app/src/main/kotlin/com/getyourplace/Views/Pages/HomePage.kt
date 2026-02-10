package com.getyourplace.Views.Pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.getyourplace.Managers.AuthManager
import com.getyourplace.Models.UserRole
import com.getyourplace.Repository.*
import com.getyourplace.ViewModels.Pages.HomePageViewModel
import com.getyourplace.ViewModels.SubPages.MatchsView
import com.getyourplace.ViewModels.SubPages.MyRentsView
import com.getyourplace.ViewModels.SubPages.SearchResidenceView
import com.getyourplace.Views.InterestsView
import com.getyourplace.Views.ProfileMainView

@Composable
fun HomePage(
    authManager: AuthManager // Passed from your NavHost or CompositionLocal
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf("home") }

    // Initialize ViewModel with dependencies
    val homeViewModel: HomePageViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomePageViewModel(
                    filterRepository = FilterRepository(),
                    residenceRepository = ResidenceRepository(context),
                    notificationRepository = NotificationRepository(),
                    chatRepository = ChatRepository(),
                    userRepository = UserRepository()
                ) as T
            }
        }
    )

    val isAuthenticated by authManager.isAuthenticated.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A)) // Red: 0.1, Green: 0.1, Blue: 0.1
    ) {
        // --- Content Area ---
        Box(modifier = Modifier.fillMaxSize()) {
            when (selectedTab) {
                "home" -> SearchResidenceView(
                    homeViewModel,
                    authManager = authManager
                )

                "rents" -> AuthGate(isAuthenticated) {
                    MyRentsView(authManager)
                }

                "heart" -> AuthGate(isAuthenticated) {
                    InterestsView()
                }

                "macths" -> AuthGate(isAuthenticated) {
                    MatchsView(authManager = authManager)
                }

                "profile" -> AuthGate(isAuthenticated) {
                    ProfileMainView(
                        profile = homeViewModel.profile,
                        authManager = authManager,
                        onProfileUpdate = { updatedProfile ->
                            homeViewModel.profile = updatedProfile
                        }
                    )
                }
            }
        }

        // --- Bottom Navigation Bar ---
        // Matches your HStack with cornerRadius(128)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp, start = 16.dp, end = 16.dp)
                .background(Color.Black.copy(alpha = 0.9f), RoundedCornerShape(128.dp))
                .padding(vertical = 8.dp, horizontal = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                MenuItem(Icons.Default.Home, "Home", selectedTab == "home") { selectedTab = "home" }
                MenuItem(Icons.Default.Key, "My Rents", selectedTab == "rents") { selectedTab = "rents" }
                MenuItem(Icons.Default.Favorite, "Interests", selectedTab == "heart") { selectedTab = "heart" }
                MenuItem(Icons.Default.ChatBubble, "Matchs", selectedTab == "macths") { selectedTab = "macths" }
                MenuItem(Icons.Default.AccountCircle, "Profile", selectedTab == "profile") { selectedTab = "profile" }
            }
        }
    }
}

@Composable
fun MenuItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) Color.White else Color.Gray,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun AuthGate(
    isAuthenticated: Boolean,
    content: @Composable () -> Unit
) {
    if (isAuthenticated) {
        content()
    } else {
        // Replace with your actual Login View or a message
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Please login to see this content", color = Color.White)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePagePreview() {
    val context = LocalContext.current
    val mockAuthManager = remember { AuthManager.mock(context,UserRole.OWNER) }

    // mockAuthManager.setAuthenticated(true)

    MaterialTheme {
        HomePage(authManager = mockAuthManager)
    }
}