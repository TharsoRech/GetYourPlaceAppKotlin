package com.getyourplace.Components.ViewModels

import android.app.Application
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

class FavoriteResidencesViewModel(application: Application) : AndroidViewModel(application) {
    private val residenceRepository: IResidenceRepository = ResidenceRepository(application)
    var favoritesResidences by mutableStateOf<List<Residence>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var isFetchingMore by mutableStateOf(false)
        private set

    init {
        getFavoritesResidences()
    }

    private fun getFavoritesResidences() {
        viewModelScope.launch {
            isLoading = true
            favoritesResidences = residenceRepository.getFavoritesResidences()
            isLoading = false
        }
    }
}