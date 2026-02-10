package com.getyourplace.Components.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getyourplace.Models.Residence
import kotlinx.coroutines.launch

class FavoriteResidencesViewModel : ViewModel() {
    var favoritesResidences by mutableStateOf<List<Residence>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var isFetchingMore by mutableStateOf(false)
        private set

    init {
        getFavoritesResidences()
    }

    fun getFavoritesResidences() {
        viewModelScope.launch {
            isLoading = true
            // results = residenceRepository.getFavoritesResidences()
            // favoritesResidences = results
            isLoading = false
        }
    }
}