package com.getyourplace.ViewModels.SubPages

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getyourplace.Models.Residence
import com.getyourplace.Repositories.Interfaces.IResidenceRepository
import com.getyourplace.Repository.ResidenceRepository
import kotlinx.coroutines.launch

class MyRentsViewModel(application: Application) : AndroidViewModel(application) {

    private val residenceRepository: IResidenceRepository = ResidenceRepository(application)

    var publishResidences by mutableStateOf<List<Residence>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var isFetchingMore by mutableStateOf(false)
        private set

    init {
        getPublishResidences()
    }

    fun getPublishResidences() {
        viewModelScope.launch {
            try {
                isLoading = true

                // Fetching data (The repository handles the IO thread)
                val results = residenceRepository.getPublishResidences()

                Log.d("MyRentsVM", "✅ Successfully fetched ${results.size} published residences")
                results.forEach { residence ->
                    Log.d("MyRentsVM", "🏠 Found Published Residence: ${residence.name} at ${residence.location}")
                }

                // Updating state (Automatically updates on Main thread)
                publishResidences = results
            } catch (e: Exception) {
                Log.e("MyRentsVM", "❌ Error fetching residences", e)
            } finally {
                isLoading = false
            }
        }
    }

    fun handleSave(residence: Residence) {
        val currentList = publishResidences.toMutableList()
        val index = currentList.indexOfFirst { it.id == residence.id }

        if (index != -1) {
            // Update existing
            currentList[index] = residence
        } else {
            // Append new
            currentList.add(residence)
        }

        publishResidences = currentList
    }
}