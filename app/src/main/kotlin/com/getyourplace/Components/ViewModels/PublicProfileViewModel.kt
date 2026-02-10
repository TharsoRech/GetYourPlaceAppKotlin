package com.getyourplace.Components.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getyourplace.Models.RentalHistory
import com.getyourplace.Models.UserReview
import com.getyourplace.Models.UserProfile
import com.getyourplace.Services.BackgroundTaskRunner
import com.getyourplace.Models.BackgroundTaskStatus
import com.getyourplace.Repository.Interfaces.IUserRepository
import com.getyourplace.Repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PublicProfileViewModel(
    private val userRepository: IUserRepository = UserRepository()
) : ViewModel() {

    // Main loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // Data lists
    private val _rentals = MutableStateFlow<List<RentalHistory>>(emptyList())
    val rentals = _rentals.asStateFlow()

    private val _reviews = MutableStateFlow<List<UserReview>>(emptyList())
    val reviews = _reviews.asStateFlow()

    private val _profile = MutableStateFlow<UserProfile?>(null)
    val profile = _profile.asStateFlow()

    // Task runners (Android equivalents to the backgroundTaskRunner in your Swift code)
    private val rentalRunner = BackgroundTaskRunner<List<RentalHistory>>()
    private val reviewRunner = BackgroundTaskRunner<List<UserReview>>()
    private val profileRunner = BackgroundTaskRunner<UserProfile>()

    init {
        fetchUserProfile()
        fetchRentals()
        fetchReviews()
        observeLoadingStates()
    }

    private fun observeLoadingStates() {
        // In Android, we can combine the status flows of our runners
        viewModelScope.launch {
            // Observe runners to update the global isLoading state
            rentalRunner.state.collect { _isLoading.value = it.status == BackgroundTaskStatus.RUNNING }
            reviewRunner.state.collect { _isLoading.value = it.status == BackgroundTaskStatus.RUNNING }
            profileRunner.state.collect { _isLoading.value = it.status == BackgroundTaskStatus.RUNNING }
        }
    }

    fun fetchRentals() {
        rentalRunner.runInBackground {
            val result = userRepository.getRentalHistory()
            _rentals.value = result
            result
        }
    }

    fun fetchReviews() {
        reviewRunner.runInBackground {
            val result = userRepository.getUserReviews()
            _reviews.value = result
            result
        }
    }

    fun fetchUserProfile() {
        profileRunner.runInBackground {
            val result = userRepository.getUserConfiguration()
            _profile.value = result
            result
        }
    }

    fun addReview(review: UserReview) {
        // Immutable list update for Compose state tracking
        val currentList = _reviews.value.toMutableList()
        currentList.add(0, review)
        _reviews.value = currentList
    }
}