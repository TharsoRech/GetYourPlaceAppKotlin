package com.getyourplace.Views.SubPages

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getyourplace.Models.Residence
import com.getyourplace.Repositories.Interfaces.IResidenceRepository
import kotlinx.coroutines.launch

class MyRentsViewModel(
    private val residenceRepository: IResidenceRepository
) : ViewModel() {

    // --- State Properties (SwiftUI @Published equivalents) ---
    var publishResidences by mutableStateOf<List<Residence>>(emptyList())
        private set // Only allow the ViewModel to modify the list

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