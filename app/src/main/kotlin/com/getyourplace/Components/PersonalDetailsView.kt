package com.getyourplace.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.getyourplace.Models.UserProfile

@Composable
fun PersonalDetailsView(
    initialProfile: UserProfile,
    onSave: (UserProfile) -> Unit
) {
    // Local state tracking the profile edits
    var profile by remember { mutableStateOf(initialProfile.copy()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 1. Standard Input (Full Name)
            CustomInputField(
                label = "FULL NAME",
                text = profile.name ?: "",
                onTextChange = { profile = profile.copy(name = it) }
            )

            // 2. Standard Input (Profession)
            CustomInputField(
                label = "PROFESSION",
                text = profile.profession ?: "",
                onTextChange = { profile = profile.copy(profession = it) }
            )

            // 3. Description Field (About Me)
            CustomDescriptionField(
                label = "ABOUT ME",
                text = profile.bio ?: "",
                onTextChange = { profile = profile.copy(bio = it) }
            )

            // 4. Dropdown Field (Date of Birth)
            // Note: If you want a DatePicker, you'd usually trigger a dialog here.
            // For now, we use your Dropdown logic.
            CustomDropdownField(
                label = "DATE OF BIRTH",
                value = profile.dob ?: "Select Date",
                options = listOf("1990", "1991", "1992", "1993", "1994", "1995"),
                onOptionSelected = { profile = profile.copy(dob = it) }
            )

            // 5. Dropdown Field (Country)
            CustomDropdownField(
                label = "COUNTRY/REGION",
                value = profile.country ?: "Select Country",
                onOptionSelected = { profile = profile.copy(country = it) }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 6. Save Button
            Button(
                onClick = { onSave(profile) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text("Save Changes", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PersonalDetailsPreview() {
    // Mock profile creation
    val mockProfile = UserProfile(
        name = "Jordan Lee",
        profession = "Architect",
        bio = "Design enthusiast living in the city.",
        dob = "Jan 01, 1995",
        country = "United States"
    )

    MaterialTheme {
        PersonalDetailsView(
            initialProfile = mockProfile,
            onSave = { updated -> println("Saved: ${updated.name}") }
        )
    }
}