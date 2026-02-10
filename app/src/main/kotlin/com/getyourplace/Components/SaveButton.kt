package com.getyourplace.Components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.getyourplace.Models.UserProfile

@Composable
fun SaveButton(
    profile: UserProfile,
    onSave: (UserProfile) -> Unit
) {
    Button(
        onClick = {
            // Equivalent to your print statement
            println("Saving: ${profile.name}")
            onSave(profile)
        },
        modifier = Modifier
            .fillMaxWidth() // Equivalent to .frame(maxWidth: .infinity)
            .height(55.dp) // Equivalent to .frame(height: 55)
            .padding(horizontal = 24.dp), // Equivalent to .padding(.horizontal, 24)
        shape = RoundedCornerShape(12.dp), // Equivalent to .cornerRadius(12)
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black, // Equivalent to .background(Color.black)
            contentColor = Color.White    // Equivalent to .foregroundColor(.white)
        )
    ) {
        Text(
            text = "Save changes",
            fontSize = 18.sp, // Headline-ish size
            fontWeight = FontWeight.Bold
        )
    }
}

// --- PREVIEW ---

@Preview(showBackground = true, backgroundColor = 0xFF808080) // Gray background for preview
@Composable
fun SaveButtonPreview() {
    SaveButton(
        profile = UserProfile(name = "Melissa Peters"),
        onSave = { updatedProfile ->
            // Handle save logic
        }
    )
}