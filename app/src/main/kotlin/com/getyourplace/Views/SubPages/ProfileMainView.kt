package com.getyourplace.Views

import androidx.compose.animation.*
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.getyourplace.Components.EditProfileView
import com.getyourplace.Components.PersonalDetailsView
import com.getyourplace.Components.ProfileImageHeader
import com.getyourplace.Components.RentalHistoryView
import com.getyourplace.Managers.AuthManager
import com.getyourplace.Models.ProfileTab // Certifique-se de que este Enum existe
import com.getyourplace.Models.UserProfile

@Composable
fun ProfileMainView(
    profile: UserProfile,
    authManager: AuthManager,
    onProfileUpdate: (UserProfile) -> Unit
) {
    var selectedTab by remember { mutableStateOf(ProfileTab.ACCOUNT) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            ProfileImageHeader()

            Spacer(modifier = Modifier.height(10.dp))

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp),
                contentPadding = PaddingValues(horizontal = 25.dp),
                horizontalArrangement = Arrangement.spacedBy(25.dp)
            ) {
                items(ProfileTab.entries) { tab ->
                    ProfileTabItem(
                        tab = tab,
                        isSelected = selectedTab == tab,
                        onClick = { selectedTab = tab }
                    )
                }
            }

            HorizontalDivider(color = Color.White.copy(alpha = 0.1f))

            Crossfade(
                targetState = selectedTab,
                animationSpec = spring(),
                modifier = Modifier.weight(1f),
                label = "ProfileTabAnimation"
            ) { targetTab ->
                when (targetTab) {
                    ProfileTab.ACCOUNT -> EditProfileView(
                        profile = profile,
                        onLogout = { authManager.logout() },
                        onSave = { updated -> onProfileUpdate(updated) }
                    )
                    ProfileTab.PERSONAL -> PersonalDetailsView(
                        initialProfile = profile,
                        onSave = { updated -> onProfileUpdate(updated) }
                    )
                    ProfileTab.RENTALS -> RentalHistoryView() // Criar este componente
                    ProfileTab.REVIEWS -> ReviewsView()      // Criar este componente
                }
            }
        }
    }
}

@Composable
fun ProfileTabItem(
    tab: ProfileTab,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.clickable(
            interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
            indication = null,
            onClick = onClick
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Nota: Adicione iconRes ao seu Enum ProfileTab ou use Icons.Default
            Text(
                text = tab.name.lowercase().replaceFirstChar { it.uppercase() },
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = if (isSelected) Color.White else Color.Gray
            )
        }

        // Underline (Rectangle do SwiftUI)
        Box(
            modifier = Modifier
                .height(2.dp)
                .width(40.dp)
                .background(if (isSelected) Color.White else Color.Transparent)
        )
    }
}