package com.getyourplace.Views

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.getyourplace.Components.FavoriteResidences
import com.getyourplace.Components.InterestedResidencesView
import com.getyourplace.Components.ResidenceDetailPopup
import com.getyourplace.Models.Residence
import com.getyourplace.Components.ViewModels.InterestedResidencesViewModel
import com.getyourplace.Components.ViewModels.FavoriteResidencesViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterestsView() {
    val scope = rememberCoroutineScope()

    // Initialize ViewModels
    val interestedVM: InterestedResidencesViewModel = viewModel()
    val favoritesVM: FavoriteResidencesViewModel = viewModel()

    var isShowingRegister by remember { mutableStateOf(false) }
    var selectedResidence by remember { mutableStateOf<Residence?>(null) }
    val pagerState = rememberPagerState(pageCount = { 2 })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = Color(0xFF1A1A1A),
                contentColor = Color.White,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                        color = Color.White
                    )
                },
                divider = {}
            ) {
                Tab(
                    selected = pagerState.currentPage == 0,
                    onClick = { scope.launch { pagerState.animateScrollToPage(0) } },
                    text = { Text("Interested") }
                )
                Tab(
                    selected = pagerState.currentPage == 1,
                    onClick = { scope.launch { pagerState.animateScrollToPage(1) } },
                    text = { Text("Favorites") }
                )
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.Top
            ) { page ->
                when (page) {
                    0 -> {
                        InterestedResidencesView(
                            viewModel = interestedVM,
                            onSelect = { res: Residence -> selectedResidence = res }
                        )
                    }
                    1 -> {
                        FavoriteResidences(
                            favViewModel = favoritesVM,
                            onSelect = { res: Residence -> selectedResidence = res }
                        )
                    }
                }
            }
        }

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
                    onEditClick = {
                        selectedResidence = residence
                        isShowingRegister = true
                    }
                )
            }
        }

        if (isShowingRegister) {
            ModalBottomSheet(
                onDismissRequest = { isShowingRegister = false },
                containerColor = Color(0xFF1A1A1A),
                dragHandle = { BottomSheetDefaults.DragHandle(color = Color.Gray) },
                modifier = Modifier.fillMaxHeight(0.95f),
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 84.dp)
                ) {
                    RegisterResidenceView(
                        residenceToEdit = null,
                        onSave = { newResidence ->
                            // viewModel.handleSave(newResidence)
                            isShowingRegister = false
                        },
                        onBack = { isShowingRegister = false }
                    )
                }
            }
        }

    }
}