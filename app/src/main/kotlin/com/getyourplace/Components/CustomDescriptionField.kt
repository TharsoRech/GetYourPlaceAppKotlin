package com.getyourplace.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomDescriptionField(
    label: String,
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Label (VStack alignment: .leading)
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        // TextEditor equivalent
        TextField(
            value = text,
            onValueChange = onTextChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp) // Fixed height for "About" section
                .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(10.dp))
                .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(10.dp)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedIndicatorColor = Color.Transparent, // Removes default underline
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(10.dp)
        )
    }
}

// --- Preview ---

@Preview(name = "About Field")
@Composable
fun CustomDescriptionFieldPreview() {
    // Local state for the preview (equivalent to @State)
    var aboutText by remember {
        mutableStateOf("I'm a UI/UX Designer based in Lagos. I love creating clean and functional interfaces for mobile apps.")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A)) // Dark background to match theme
            .padding(16.dp)
    ) {
        CustomDescriptionField(
            label = "ABOUT",
            text = aboutText,
            onTextChange = { aboutText = it }
        )
    }
}