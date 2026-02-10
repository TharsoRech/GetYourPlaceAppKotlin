package com.getyourplace.Components.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getyourplace.Models.Conversation
import com.getyourplace.Models.EngagementStatus
import com.getyourplace.Models.InterestedProfile
import com.getyourplace.Repository.Interfaces.IChatRepository
import com.getyourplace.Repository.Interfaces.IMatchsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MatchsResidencesViewModel(
    private val chatRepository: IChatRepository,
    private val matchsRepository: IMatchsRepository
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    var profiles by mutableStateOf<List<InterestedProfile>>(emptyList())
    var conversations by mutableStateOf<List<Conversation>>(emptyList())
    var selectedChatProfile by mutableStateOf<InterestedProfile?>(null)

    init {
        fetchAllData()
    }

    private fun fetchAllData() {
        viewModelScope.launch {
            isLoading = true
            // Launch both fetches in parallel
            val conversationsDeferred = async { chatRepository.getConversations() }
            val matchsDeferred = async { matchsRepository.getMatchs() }

            conversations = conversationsDeferred.await()
            profiles = matchsDeferred.await()
            isLoading = false
        }
    }

    suspend fun getConversation(profile: InterestedProfile): Conversation {
        isLoading = true
        val conversation = chatRepository.getConversation(profile)
        isLoading = false
        return conversation
    }

    fun updateProfileStatus(profile: InterestedProfile, newStatus: EngagementStatus) {
        // Find the profile in the list and update its status
        val updatedList = profiles.map {
            if (it.id == profile.id) it.copy(status = newStatus) else it
        }
        profiles = updatedList

        // Optional: Call your repository here to save the change to the database/API
        // viewModelScope.launch { matchsRepository.updateStatus(profile.id, newStatus) }
    }
}