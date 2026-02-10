package com.getyourplace.Components.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getyourplace.Models.UserReview
import com.getyourplace.Repository.Interfaces.IUserRepository
import com.getyourplace.Repository.UserRepository
import kotlinx.coroutines.launch

class ReviewsViewModel(
    private val userRepository: IUserRepository = UserRepository()
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    var reviews by mutableStateOf<List<UserReview>>(emptyList())
        private set

    fun fetchReviews() {
        if (isLoading) return

        viewModelScope.launch {
            isLoading = true
            try {
                // Assuming your repository is a suspend function
                reviews = userRepository.getUserReviews()
            } catch (e: Exception) {
                // Handle error
            } finally {
                isLoading = false
            }
        }
    }
}