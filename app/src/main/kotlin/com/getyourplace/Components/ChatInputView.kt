package com.getyourplace.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatInputView(
    text: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
    ) {
        HorizontalDivider(thickness = 0.5.dp, color = Color.DarkGray)

        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Bottom, // Aligns button to bottom of text field
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TextField(
                value = text,
                onValueChange = onTextChange,
                modifier = Modifier
                    .weight(1f) // Takes up remaining space
                    .background(Color(0xFF1C1C1E), RoundedCornerShape(20.dp)),
                placeholder = { Text("Message...", color = Color.Gray) },
                maxLines = 5,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    cursorColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedIndicatorColor = Color.Transparent, // Removes underline
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            IconButton(
                onClick = onSend,
                enabled = text.isNotEmpty(),
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = if (text.isEmpty()) Color.Gray else Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Preview(name = "Static Preview - Empty")
@Composable
fun ChatInputViewPreview() {
    ChatInputView(
        text = "",
        onTextChange = {},
        onSend = {}
    )
}

@Preview(name = "Interactive Typing Test")
@Composable
fun ChatInputViewInteractivePreview() {
    // This state allows the preview to actually respond to typing
    var testText by remember { mutableStateOf("Hey, check this out!") }

    Box(modifier = Modifier.background(Color.Gray).padding(20.dp)) {
        ChatInputView(
            text = testText,
            onTextChange = { testText = it },
            onSend = { testText = "" }
        )
    }
}