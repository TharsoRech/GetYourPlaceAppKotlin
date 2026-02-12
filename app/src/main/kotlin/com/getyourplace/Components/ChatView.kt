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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.getyourplace.Models.ChatMessage
import java.util.Date

@Composable
fun ChatView(
    title: String,
    messages: MutableList<ChatMessage>
) {
    var newMessageText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A)) // Cor escura para combinar com o tema
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding() // Faz o layout subir quando o teclado aparece
        ) {
            // --- HEADER ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            HorizontalDivider(color = Color.White.copy(alpha = 0.1f))

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

            ChatInputView(
                text = newMessageText,
                onTextChange = { newMessageText = it },
                onSend = {
                    if (newMessageText.isNotBlank()) {
                        // Adiciona a nova mensagem diretamente à lista mutável
                        messages.add(
                            ChatMessage(
                                text = newMessageText.trim(),
                                isSender = true,
                                timestamp = Date()
                            )
                        )
                        newMessageText = ""
                    }
                }
            )

            // Espaçamento inferior para garantir que não fique colado na barra de navegação
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ChatViewPreview() {
    // Mock data for the preview
    val mockMessages = remember {
        mutableStateListOf(
            ChatMessage(text = "Hello! Is this still available?", isSender = false, timestamp = Date()),
            ChatMessage(text = "Yes, it is. When would you like to see it?", isSender = true, timestamp = Date()),
            ChatMessage(text = "How about tomorrow at 2 PM?", isSender = false, timestamp = Date()),
            ChatMessage(text = "That works for me! See you then.", isSender = true, timestamp = Date())
        )
    }

    MaterialTheme {
        ChatView(
            title = "John Doe",
            messages = mockMessages
        )
    }
}