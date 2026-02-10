package com.getyourplace.Components.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getyourplace.Models.Conversation
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ConversationListViewModel : ViewModel() {
    var isLoading by mutableStateOf(false)
        private set

    var conversations by mutableStateOf<List<Conversation>>(emptyList())
        private set

    init {
        fetchConversations()
    }

    fun fetchConversations() {
        viewModelScope.launch {
            isLoading = true
            // Simulate your BackgroundTaskRunner
            delay(1000)
            // conversations = chatRepository.getConversations()
            isLoading = false
        }
    }
}