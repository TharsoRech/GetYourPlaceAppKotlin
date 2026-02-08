package com.getyourplace.Repository

import com.getyourplace.Models.ChatMessage
import com.getyourplace.Models.Conversation
import com.getyourplace.Models.InterestedProfile
import com.getyourplace.Repository.Interfaces.IChatRepository
import kotlinx.coroutines.delay
import java.util.Date

class ChatRepository : IChatRepository {

    override suspend fun getConversation(profile: InterestedProfile): Conversation {
        delay(2000)

        return Conversation(
            name = profile.name,
            time = "10:24 AM",
            imageName = "person_circle_fill",
            unreadCount = 2,
            conversationMessages = listOf(
                ChatMessage(
                    text = "Hey! How's the project going?",
                    isSender = false,
                    timestamp = Date(System.currentTimeMillis() - 3600 * 1000)
                ),
                ChatMessage(
                    text = "Are we still meeting at 5?",
                    isSender = false,
                    timestamp = Date()
                )
            )
        )
    }

    override suspend fun getConversations(): List<Conversation> {
        delay(2000)

        return listOf(
            Conversation(
                name = "James Wilson",
                time = "10:24 AM",
                imageName = "person_circle_fill",
                unreadCount = 2,
                conversationMessages = listOf(
                    ChatMessage(text = "Hey! How's the project going?", isSender = false, timestamp = Date(System.currentTimeMillis() - 3600 * 1000)),
                    ChatMessage(text = "Are we still meeting at 5?", isSender = false, timestamp = Date())
                )
            ),
            Conversation(
                name = "Sarah Parker",
                time = "Yesterday",
                imageName = "person_crop_circle",
                unreadCount = 0,
                conversationMessages = listOf(
                    ChatMessage(text = "The new UI looks great!", isSender = false, timestamp = Date(System.currentTimeMillis() - 86400 * 1000))
                )
            ),
            Conversation(
                name = "Tech Support",
                time = "Monday",
                imageName = "headphones",
                unreadCount = 1,
                conversationMessages = listOf(
                    ChatMessage(text = "Your ticket #1234 has been updated.", isSender = false, timestamp = Date(System.currentTimeMillis() - 172800 * 1000))
                )
            )
        )
    }
}