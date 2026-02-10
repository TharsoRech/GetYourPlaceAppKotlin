package com.getyourplace.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.getyourplace.Components.ViewModels.ConversationListViewModel
import com.getyourplace.Models.Conversation

@Composable
fun ConversationsListView(
    activeConversation: Conversation?,
    onConversationSelected: (Conversation) -> Unit,
    listViewModel: ConversationListViewModel = viewModel()
) {
    ConversationsListContent(
        isLoading = listViewModel.isLoading,
        conversations = listViewModel.conversations,
        onChatClick = onConversationSelected
    )
}

@Composable
fun ConversationsListContent(
    isLoading: Boolean,
    conversations: List<Conversation>,
    onChatClick: (Conversation) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            if (isLoading) {
                items(5) { ConversationRowSkeleton() }
            } else {
                items(conversations) { chat ->
                    // Wrap in Box to handle the click ripple effect correctly
                    Box(modifier = Modifier.clickable { onChatClick(chat) }) {
                        ConversationRow(conversation = chat)
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = 0.5.dp,
                        color = Color.White.copy(alpha = 0.1f)
                    )
                }
            }
        }
    }
}

@Composable
fun ConversationRow(conversation: Conversation) {
    ListItem(
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        leadingContent = {
            // Placeholder for User Image
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color.Gray.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Text(conversation.name.take(1), color = Color.White)
            }
        },
        headlineContent = {
            Text(
                text = conversation.name,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        },
        supportingContent = {
            Text(
                text = conversation.lastMessage,
                color = Color.Gray,
                maxLines = 1
            )
        },
        trailingContent = {
            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = conversation.time, color = Color.Gray, fontSize = 12.sp)
                if (conversation.unreadCount > 0) {
                    Badge(containerColor = Color.Red, contentColor = Color.White) {
                        Text("${conversation.unreadCount}")
                    }
                }
            }
        }
    )
}

@Composable
fun ConversationRowSkeleton() {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(50.dp).background(Color.Gray.copy(0.2f), CircleShape))
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Box(modifier = Modifier.size(120.dp, 12.dp).background(Color.Gray.copy(0.2f), RoundedCornerShape(4.dp)))
            Spacer(modifier = Modifier.height(8.dp))
            Box(modifier = Modifier.fillMaxWidth(0.7f).size(10.dp).background(Color.Gray.copy(0.1f), RoundedCornerShape(4.dp)))
        }
    }
}

// --- PREVIEW ---

@Preview(showBackground = true)
@Composable
fun ConversationsListPreview() {
    // Correctly using the .mock() function you created in the Model
    val mockChats = listOf(
        Conversation.mock(),
        Conversation.mock().apply {
            name = "Alex Rivera"
            unreadCount = 0
        }
    )

    MaterialTheme {
        ConversationsListContent(
            isLoading = false,
            conversations = mockChats,
            onChatClick = {}
        )
    }
}