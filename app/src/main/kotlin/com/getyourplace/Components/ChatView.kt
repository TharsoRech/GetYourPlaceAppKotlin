package com.getyourplace.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.getyourplace.Models.ChatMessage
import java.util.Date

@Composable
fun ChatView(
    title: String,
    messages: MutableList<ChatMessage>
) {
    var newMessageText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Auto-scroll to bottom when messages list size changes
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A)) // Color(red: 0.1, green: 0.1, blue: 0.1)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            HorizontalDivider(color = Color.Gray.copy(alpha = 0.3f))

            // Message List
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(messages) { message ->
                    ChatBubble(message = message)
                }
            }

            // Input View
            ChatInputView(
                text = newMessageText,
                onTextChange = { newMessageText = it },
                onSend = {
                    if (newMessageText.isNotBlank()) {
                        messages.add(
                            ChatMessage(
                                text = newMessageText,
                                isSender = true,
                                timestamp = Date()
                            )
                        )
                        newMessageText = ""
                        // The LaunchedEffect above will handle the scroll!
                    }
                }
            )

            // Padding for system bars if needed (similar to .padding(.bottom, 84))
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}