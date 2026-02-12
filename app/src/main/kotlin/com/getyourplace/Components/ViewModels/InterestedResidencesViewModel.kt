package com.getyourplace.Components.ViewModels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel // Change this
import androidx.lifecycle.viewModelScope
import com.getyourplace.Models.Residence
import com.getyourplace.Repositories.Interfaces.IResidenceRepository
import com.getyourplace.Repository.ResidenceRepository
import kotlinx.coroutines.launch


class InterestedResidencesViewModel(application: Application) : AndroidViewModel(application) {

    private val residenceRepository: IResidenceRepository = ResidenceRepository(application)

    var interestedResidences by mutableStateOf<List<Residence>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var isFetchingMore by mutableStateOf(false)
        private set

    init {
        getInterestedResidences()
    }

    private fun getInterestedResidences() {
        viewModelScope.launch {
            isLoading = true
            try {
                interestedResidences = residenceRepository.getInterestedResidences()
            } catch (e: Exception) {
                // Handle potential errors
            } finally {
                isLoading = false
            }
        }
    }
}