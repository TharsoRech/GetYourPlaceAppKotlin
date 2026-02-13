package com.getyourplace.Components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.getyourplace.Models.UserProfile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileView(
    profile: UserProfile,
    onLogout: () -> Unit,
    onSave: (UserProfile) -> Unit
) {
    var email by remember { mutableStateOf(profile.email ?: "") }
    var password by remember { mutableStateOf(profile.password ?: "") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Edit Profile", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFF1A1A1A))
            )
        },
        containerColor = Color(0xFF1A1A1A),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(25.dp)
        ) {
            CustomInputField(
                label = "Email",
                text = email,
                onTextChange = { email = it }
            )

            CustomInputField(
                label = "Password",
                text = password,
                onTextChange = { password = it },
                isSecure = true
            )

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ActionButton(text = "Log Out", textColor = Color.White, onClick = onLogout)
                ActionButton(text = "Remove Account", textColor = Color.Red, onClick = onLogout)
                ActionButton(text = "Save Changes", textColor = Color.White, onClick = { onSave} )
            }
        }
    }
}

@Composable
fun ActionButton(text: String, textColor: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
    ) {
        Text(text = text, color = textColor, fontWeight = FontWeight.Bold)
    }
}

// --- PREVIEW SECTION ---

@Preview(showBackground = true, backgroundColor = 0xFF1A1A1A)
@Composable
fun EditProfileViewPreview() {
    // 1. Create a mock user to populate the fields
    val mockUser = UserProfile.mock()

    // 2. Wrap in MaterialTheme so the UI components (Buttons, Scaffold) look correct
    MaterialTheme {
        EditProfileView(
            profile = mockUser,
            onLogout = {
                // In preview, we just print to console
                println("Logout clicked")
            },
            onSave = { updatedProfile ->
                // This simulates the save action
                println("Saving profile for: ${updatedProfile.email}")
            }
        )
    }
}