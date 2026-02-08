package com.getyourplace.Repository.Interfaces

import com.getyourplace.Models.Conversation
import com.getyourplace.Models.InterestedProfile

interface IChatRepository {
    suspend fun getConversations(): List<Conversation>

    suspend fun getConversation(profile: InterestedProfile): Conversation
}