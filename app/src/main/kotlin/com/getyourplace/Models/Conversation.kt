package com.getyourplace.Models

import java.util.UUID

data class Conversation(
    // Using UUID for unique identification in lists
    val id: UUID = UUID.randomUUID(),
    var name: String,
    var time: String,
    var imageName: String,
    var unreadCount: Int,
    var conversationMessages: List<ChatMessage>
) {
    /**
     * Computed property equivalent to Swift:
     * var lastMessage: String { conversationMessages.last?.text ?? "No messages yet" }
     */
    val lastMessage: String
        get() = conversationMessages.lastOrNull()?.text ?: "No messages yet"

    companion object {
        fun mock() = Conversation(
            name = "Jordan Lee",
            time = "10:30 AM",
            imageName = "person_crop_circle",
            unreadCount = 3,
            conversationMessages = listOf(
                ChatMessage.mock(),
                ChatMessage.mock(),
                ChatMessage.mock()
            )
        )
    }
}