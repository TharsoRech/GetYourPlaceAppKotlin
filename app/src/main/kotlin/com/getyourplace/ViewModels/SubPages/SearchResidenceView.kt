package com.getyourplace.ViewModels.SubPages

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.getyourplace.Components.*
import com.getyourplace.Managers.AuthManager
import com.getyourplace.Models.Residence
import com.getyourplace.ViewModels.Pages.HomePageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResidenceView(
    viewModel: HomePageViewModel,
    authManager: AuthManager
) {
    var selectedResidence by remember { mutableStateOf<Residence?>(null) }
    val isAuthenticated by authManager.isAuthenticated.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
    ) {
        // --- 1. CONTEÚDO PRINCIPAL ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Explore", color = Color.White, fontSize = 22.sp)
                Spacer(modifier = Modifier.size(45.dp))
            }

            // Barra de Busca
            CustomSearchBar(
                text = viewModel.searchText,
                onTextChange = { viewModel.searchText = it },
                onSearchTap = { viewModel.performSearch() },
                onFilterTap = { viewModel.filterClicked() },
                isFilterActive = viewModel.isFilterActive
            )

            // Filtros Rápidos (Horizontal)
            ClickFilterList(
                filters = viewModel.filters,
                onClickFilter = { filter ->
                    viewModel.applyDefaultFilter(filter)
                }
            )

            // Lista de Residências
            ResidenceListView(
                residences = viewModel.residences,
                isLoading = viewModel.isLoading,
                isFetchingMore = viewModel.isFetchingMore,
                onLoadMore = { viewModel.loadNextPage() },
                onSelect = { selectedResidence = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            )
        }

        // --- 2. BOTÃO DE NOTIFICAÇÃO (Z-Stack) ---
        if (isAuthenticated) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 10.dp, end = 32.dp)
                    .zIndex(1f)
            ) {
                NotificationButton(
                    notifications = viewModel.newNotifications,
                    onNotificationUpdate = { newList ->
                        viewModel.newNotifications = newList
                    }
                )
            }
        }

        // --- 3. POPUP DE DETALHES ---
        AnimatedVisibility(
            visible = selectedResidence != null,
            enter = fadeIn() + scaleIn(initialScale = 0.9f),
            exit = fadeOut() + scaleOut(targetScale = 1.1f),
            modifier = Modifier.zIndex(100f)
        ) {
            selectedResidence?.let { residence ->
                ResidenceDetailPopup(
                    residence = residence,
                    onDismiss = { selectedResidence = null },
                    onEditClick = { resToEdit ->
                        // Lógica para abrir tela de edição
                        selectedResidence = null
                        // Ex: navigation.navigate("Edit", resToEdit)
                    }
                )
            }
        }

        // --- 4. BOTTOM SHEET DE FILTROS ---
        if (viewModel.showingFilters) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.showingFilters = false },
                containerColor = Color(0xFF1A1A1A),
                dragHandle = { BottomSheetDefaults.DragHandle(color = Color.Gray) }
            ) {
                FilterView(
                    initialFilter = viewModel.currentFilter,
                    onDismiss = { viewModel.showingFilters = false },
                    onApplyChanges = { updatedFilter, isApplied ->
                        viewModel.currentFilter = updatedFilter
                        viewModel.applyCustomFilters(isApplied)
                        viewModel.showingFilters = false
                    }
                )
            }
        }
    }
}