package com.getyourplace.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.getyourplace.Models.ChatMessage
import java.util.Date
import java.util.UUID

@Composable
fun ChatBubble(message: ChatMessage) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        // Equivalent to the Spacer() logic: pushes to start or end
        horizontalArrangement = if (message.isSender) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                // Equivalent to .padding(leading/trailing, 60)
                .padding(
                    start = if (message.isSender) 60.dp else 0.dp,
                    end = if (message.isSender) 0.dp else 60.dp
                )
                .background(
                    color = if (message.isSender) Color.Black else Color(0xFFE5E5EA), // Gray5 equivalent
                    shape = RoundedCornerShape(18.dp)
                )
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Text(
                text = message.text,
                color = if (message.isSender) Color.White else Color.Black,
                fontSize = 16.sp
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun ChatBubblePreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Example 1: Received message (Left side, Gray)
        ChatBubble(
            message = ChatMessage(
                id = UUID.randomUUID().toString(),
                text = "Hi! Is this apartment still available for next month?",
                isSender = false,
                timestamp = Date()
            )
        )

        // Example 2: Sent message (Right side, Black)
        ChatBubble(
            message = ChatMessage(
                id = UUID.randomUUID().toString(),
                text = "Yes, it is! Would you like to schedule a viewing?",
                isSender = true,
                timestamp = Date()
            )
        )

        // Example 3: Short sent message
        ChatBubble(
            message = ChatMessage(
                id = UUID.randomUUID().toString(),
                text = "Great!",
                isSender = true,
                timestamp = Date()
            )
        )
    }
}