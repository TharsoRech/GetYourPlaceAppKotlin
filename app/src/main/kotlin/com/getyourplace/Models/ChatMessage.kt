package com.getyourplace.Models

import java.util.UUID
import java.util.Date

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val isSender: Boolean,
    val timestamp: Date
) {
    companion object {
        fun mock() = ChatMessage(
            text = "Hey! Is the property still available for this weekend?",
            isSender = false,
            timestamp = Date()
        )
    }
}