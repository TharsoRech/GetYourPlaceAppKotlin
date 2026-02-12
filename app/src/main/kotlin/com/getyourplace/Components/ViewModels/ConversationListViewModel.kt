package com.getyourplace.Components.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getyourplace.Models.Conversation
import com.getyourplace.Repository.ChatRepository
import com.getyourplace.Repository.Interfaces.IChatRepository
import com.getyourplace.Repository.Interfaces.IUserRepository
import com.getyourplace.Repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ConversationListViewModel : ViewModel() {
    private val chatRepository: IChatRepository = ChatRepository()
    var isLoading by mutableStateOf(false)
        private set

    var conversations by mutableStateOf<List<Conversation>>(emptyList())
        private set

    init {
        fetchConversations()
    }

    private fun fetchConversations() {
        viewModelScope.launch {
            isLoading = true
            // Simulate your BackgroundTaskRunner
            delay(1000)
            conversations = chatRepository.getConversations()
            isLoading = false
        }
    }
}