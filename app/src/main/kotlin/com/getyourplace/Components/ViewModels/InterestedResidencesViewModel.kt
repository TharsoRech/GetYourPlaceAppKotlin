package com.getyourplace.Components.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getyourplace.Models.Residence
import kotlinx.coroutines.launch

class InterestedResidencesViewModel : ViewModel() {
    var interestedResidences by mutableStateOf<List<Residence>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var isFetchingMore by mutableStateOf(false)
        private set

    init {
        getInterestedResidences()
    }

    fun getInterestedResidences() {
        viewModelScope.launch {
            isLoading = true
            try {
                // Replace with: interestedResidences = residenceRepository.getInterestedResidences()
                // For now, it stays empty or you can load mocks here if testing
            } finally {
                isLoading = false
            }
        }
    }
}