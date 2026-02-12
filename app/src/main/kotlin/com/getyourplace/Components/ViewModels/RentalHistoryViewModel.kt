package com.getyourplace.Components.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getyourplace.Models.RentalHistory
import com.getyourplace.Repository.Interfaces.IUserRepository
import com.getyourplace.Repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RentalHistoryViewModel : ViewModel() {
    private val userRepository: IUserRepository = UserRepository()
    var rentals by mutableStateOf<List<RentalHistory>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    init {
        fetchRentals()
    }

    fun fetchRentals() {
        if (isLoading) return

        viewModelScope.launch {
            isLoading = true
            // Simulate network delay
            delay(1500)
            // Mock data fetch
             rentals = userRepository.getRentalHistory()
            isLoading = false
        }
    }

    fun addRental(rental: RentalHistory) {
        rentals = listOf(rental) + rentals
    }

    fun deleteRental(rental: RentalHistory) {
        rentals = rentals.filter { it.id != rental.id }
    }
}