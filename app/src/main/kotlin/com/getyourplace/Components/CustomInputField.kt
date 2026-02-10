package com.getyourplace.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomInputField(
    label: String,
    text: String,
    onTextChange: (String) -> Unit,
    isSecure: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Label
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        // Input Field
        TextField(
            value = text,
            onValueChange = onTextChange,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(10.dp))
                .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(10.dp)),
            // Logic for password hiding
            visualTransformation = if (isSecure) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                keyboardType = if (isSecure) KeyboardType.Password else KeyboardType.Text
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true,
            shape = RoundedCornerShape(10.dp)
        )
    }
}

// --- Previews ---

@Preview(name = "Standard Input")
@Composable
fun CustomInputFieldPreview() {
    var name by remember { mutableStateOf("Melissa Peters") }

    Box(modifier = Modifier.background(Color(0xFF1A1A1A)).padding(16.dp)) {
        CustomInputField(
            label = "FULL NAME",
            text = name,
            onTextChange = { name = it }
        )
    }
}

@Preview(name = "Secure Input")
@Composable
fun CustomSecureFieldPreview() {
    var password by remember { mutableStateOf("password123") }

    Box(modifier = Modifier.background(Color(0xFF1A1A1A)).padding(16.dp)) {
        CustomInputField(
            label = "PASSWORD",
            text = password,
            isSecure = true,
            onTextChange = { password = it }
        )
    }
}