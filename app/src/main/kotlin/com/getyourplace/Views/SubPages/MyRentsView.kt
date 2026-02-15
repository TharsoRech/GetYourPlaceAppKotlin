package com.getyourplace.Views.SubPages

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.getyourplace.Components.AccordionView
import com.getyourplace.Components.ResidenceDetailPopup
import com.getyourplace.Components.ResidenceListView
import com.getyourplace.Managers.AuthManager
import com.getyourplace.Models.Residence
import com.getyourplace.Models.UserRole
import com.getyourplace.ViewModels.SubPages.MyRentsViewModel
import com.getyourplace.Views.RegisterResidenceView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyRentsView(
    authManager: AuthManager,
    viewModel: MyRentsViewModel = viewModel()
) {
    val currentUser by authManager.currentUser.collectAsState()
    var selectedResidence by remember { mutableStateOf<Residence?>(null) }
    var isShowingRegister by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 100.dp)
        ) {
            if (currentUser?.role == UserRole.OWNER) {
                Text(
                    text = "My Properties",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )

                OwnerContent(viewModel) { selectedResidence = it }
            } else {
                RenterContent(viewModel) { selectedResidence = it }
            }
        }

        if (currentUser?.role == UserRole.OWNER) {
            FloatingActionButton(
                onClick = { isShowingRegister = true },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 128.dp, end = 24.dp)
                    .size(60.dp),
                containerColor = Color.White,
                contentColor = Color.Black,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    modifier = Modifier.size(30.dp)
                )
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
                onDismissRequest = {
                    isShowingRegister = false
                    if (selectedResidence != null) selectedResidence = null
                },
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
                        residenceToEdit = selectedResidence,
                        onSave = { updatedResidence ->
                            viewModel.handleSave(updatedResidence)
                            isShowingRegister = false
                            selectedResidence = null // Clear selection after save
                        },
                        onBack = {
                            isShowingRegister = false
                            selectedResidence = null
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun OwnerContent(viewModel: MyRentsViewModel, onSelect: (Residence) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        AccordionView (title = "Published Properties") {
            ResidenceListView(
                residences = viewModel.publishResidences.filter { it.isPublished },
                isLoading = viewModel.isLoading,
                isFetchingMore = viewModel.isFetchingMore,
                isScrollable = false,
                onLoadMore = { },
                onSelect = onSelect
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        AccordionView(title = "Unpublished Properties") {
            ResidenceListView(
                residences = viewModel.publishResidences.filter { !it.isPublished },
                isLoading = viewModel.isLoading,
                isFetchingMore = viewModel.isFetchingMore,
                isScrollable = false,
                onLoadMore = { },
                onSelect = onSelect
            )
        }
    }
}

@Composable
fun RenterContent(viewModel: MyRentsViewModel, onSelect: (Residence) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "My Rents",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(20.dp))

        ResidenceListView(
            residences = viewModel.publishResidences,
            isLoading = viewModel.isLoading,
            isFetchingMore = viewModel.isFetchingMore,
            isScrollable = false,
            onLoadMore = { },
            onSelect = onSelect
        )
    }
}
