package com.getyourplace.ViewModels.SubPages

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import com.getyourplace.Components.ResidenceDetailPopup
import com.getyourplace.Components.ResidenceListView
import com.getyourplace.Managers.AuthManager
import com.getyourplace.Models.Residence
import com.getyourplace.Models.UserRole
import com.getyourplace.Views.RegisterResidenceView
import com.getyourplace.Views.SubPages.MyRentsViewModel

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
        // --- 1. CONTEÚDO PRINCIPAL ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 100.dp) // Espaço para não cobrir pelo FAB ou Nav
        ) {
            if (currentUser?.role == UserRole.OWNER) {
                // Título para o Owner
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

        // --- 2. FLOATING ACTION BUTTON (Botão de Plus) ---
        if (currentUser?.role == UserRole.OWNER) {
            FloatingActionButton(
                onClick = { isShowingRegister = true },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 32.dp, end = 24.dp)
                    .size(60.dp),
                containerColor = Color.White,
                contentColor = Color.Black,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(30.dp))
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
                    onEditClick = {
                        // Se clicar em editar, fecha o detalhe e abre o registro
                        selectedResidence = null
                        isShowingRegister = true
                    }
                )
            }
        }

        // --- 4. SHEET DE REGISTRO (MODAL) ---
        if (isShowingRegister) {
            ModalBottomSheet(
                onDismissRequest = { isShowingRegister = false },
                containerColor = Color(0xFF1A1A1A),
                dragHandle = { BottomSheetDefaults.DragHandle(color = Color.Gray) },
                modifier = Modifier.fillMaxHeight(0.95f) // Quase tela cheia
            ) {
                RegisterResidenceView(
                    residenceToEdit = null, // Pode passar o residence se for editar
                    onSave = { newResidence ->
                        viewModel.handleSave(newResidence)
                        isShowingRegister = false
                    },
                    onBack = { isShowingRegister = false }
                )
            }
        }
    }
}

@Composable
fun OwnerContent(viewModel: MyRentsViewModel, onSelect: (Residence) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        SimpleAccordion(title = "Published Properties") {
            ResidenceListView(
                residences = viewModel.publishResidences.filter { it.isPublished },
                isLoading = viewModel.isLoading,
                isFetchingMore = viewModel.isFetchingMore,
                isScrollable = false, // CRÍTICO: Column já tem scroll
                onLoadMore = { },
                onSelect = onSelect
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        SimpleAccordion(title = "Unpublished Properties") {
            ResidenceListView(
                residences = viewModel.publishResidences.filter { !it.isPublished },
                isLoading = viewModel.isLoading,
                isFetchingMore = viewModel.isFetchingMore,
                isScrollable = false, // CRÍTICO: Column já tem scroll
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
            isScrollable = false, // Evita conflito de scroll
            onLoadMore = { },
            onSelect = onSelect
        )
    }
}

@Composable
fun SimpleAccordion(title: String, content: @Composable () -> Unit) {
    var expanded by remember { mutableStateOf(true) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(8.dp))
                .clickable { expanded = !expanded }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = Color.White
            )
        }
        AnimatedVisibility(visible = expanded) {
            Box(modifier = Modifier.padding(top = 12.dp)) {
                content()
            }
        }
    }
}