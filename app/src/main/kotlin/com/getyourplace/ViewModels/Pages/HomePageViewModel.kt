package com.getyourplace.ViewModels.Pages

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getyourplace.Models.*
import com.getyourplace.Repositories.Interfaces.IResidenceRepository
import com.getyourplace.Repository.Interfaces.*
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

class HomePageViewModel(
    private val filterRepository: IFilterRepository,
    private val residenceRepository: IResidenceRepository,
    private val notificationRepository: INotificationRepository,
    private val chatRepository: IChatRepository,
    private val userRepository: IUserRepository
) : ViewModel() {

    // --- State Properties (SwiftUI @Published) ---
    var searchText by mutableStateOf("")
    var filters by mutableStateOf<List<String>>(emptyList())
    var defaultFilter by mutableStateOf("")
    var residences by mutableStateOf<List<Residence>>(emptyList())
    var allResidences by mutableStateOf<List<Residence>>(emptyList())
    var showingFilters by mutableStateOf(false)
    var currentFilter by mutableStateOf(ResidenceFilter())
    var isFilterActive by mutableStateOf(false)
    var isLoading by mutableStateOf(false)
    var isFetchingMore by mutableStateOf(false)
    var newNotifications by mutableStateOf<List<String>>(emptyList())
    var profile by mutableStateOf(UserProfile())

    // --- Pagination State ---
    private var currentPage = 1
    private var canLoadMore = true

    init {
        initialFetch()
    }

    private fun initialFetch() {
        viewModelScope.launch {
            isLoading = true
            // Dispara todos os fetches em paralelo, similar ao Task do Swift
            val jobs = listOf(
                launch { fetchFilters() },
                launch { getRecentResidences() },
                launch { fetchCustomFilters() },
                launch { fetchNotifications() },
                launch { fetchUserProfile() }
            )
            jobs.joinAll()
            isLoading = false
        }
    }

    // --- Funções de UI (SwiftUI equivalents) ---

    fun filterClicked() {
        showingFilters = !showingFilters
    }

    fun applyDefaultFilter(filter: String) {
        defaultFilter = filter
        Log.d("HomePageVM", "Custom Filter clicked: $defaultFilter")
        orderResidences()
    }

    fun performSearch() {
        Log.d("HomePageVM", "Searching for: $searchText")

        if (searchText.isEmpty()) {
            if (isFilterActive) {
                filterResidences()
            } else {
                residences = allResidences
                orderResidences()
            }
            return
        }

        val filtered = allResidences.filter {
            it.name.contains(searchText, ignoreCase = true) ||
                    it.address.contains(searchText, ignoreCase = true) ||
                    it.location.contains(searchText, ignoreCase = true)
        }

        viewModelScope.launch {
            residences = if (isFilterActive) {
                residenceRepository.filterResidences(filtered, currentFilter)
            } else {
                filtered
            }
            orderResidences()
        }
    }

    fun orderResidences() {
        isLoading = true
        val currentList = residences.toMutableList()
        when (defaultFilter) {
            "Price" -> currentList.sortBy { it.price }
            "Newest" -> currentList.sortByDescending { it.createdAt }
            "Rating" -> currentList.sortByDescending { it.rating }
            "Category" -> currentList.sortBy { it.type }
            else -> currentList.sortByDescending { it.createdAt }
        }
        residences = currentList
        isLoading = false
    }

    fun applyCustomFilters(isApplied: Boolean) {
        this.isFilterActive = isApplied
        if (isApplied) {
            filterResidences()
            Log.d("HomePageVM", "filter set to Residences")
        } else {
            residences = allResidences
            orderResidences()
            Log.d("HomePageVM", "filter set to allResidences")
        }
    }

    // --- Data Fetching (Repository interaction) ---

    fun getRecentResidences() = viewModelScope.launch {
        isLoading = true
        val results = residenceRepository.getRecentResidences()
        allResidences = results
        residences = results
        orderResidences()
        isLoading = false
    }

    private fun fetchFilters() = viewModelScope.launch {
        filters = filterRepository.getDefaultFilters()
    }

    private fun fetchCustomFilters() = viewModelScope.launch {
        currentFilter = filterRepository.getCustomFilters()
    }

    fun filterResidences() = viewModelScope.launch {
        isLoading = true
        residences = residenceRepository.filterResidences(allResidences, currentFilter)
        orderResidences() // Adicionado para manter a ordenação após filtrar
        isLoading = false
    }

    private fun fetchNotifications() = viewModelScope.launch {
        newNotifications = notificationRepository.getNotifications()
    }

    private fun fetchUserProfile() = viewModelScope.launch {
        profile = userRepository.getUserConfiguration()
    }

    // --- Faltava a Paginação ---
    fun loadNextPage() {
        if (isFetchingMore || isLoading || !canLoadMore) return

        viewModelScope.launch {
            isFetchingMore = true
            val nextPage = currentPage + 1

            // Aqui você passaria o parâmetro de página para o seu repositório
            val results = residenceRepository.getRecentResidences() // Ex: getRecentResidences(page = nextPage)

            if (results.isEmpty()) {
                canLoadMore = false
            } else {
                residences = residences + results
                currentPage = nextPage
            }
            isFetchingMore = false
        }
    }
}