package com.getyourplace.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.getyourplace.Models.Residence
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.LocalContext
import com.getyourplace.Components.ViewModels.FavoriteResidencesViewModel

@Composable
fun FavoriteResidences(
    onSelect: (Residence) -> Unit,
    favViewModel: FavoriteResidencesViewModel = viewModel()
) {
    // Hoisting state for the Content/Preview
    FavoriteResidencesContent(
        residences = favViewModel.favoritesResidences,
        isLoading = favViewModel.isLoading,
        isFetchingMore = favViewModel.isFetchingMore,
        onSelect = onSelect
    )
}

@Composable
fun FavoriteResidencesContent(
    residences: List<Residence>,
    isLoading: Boolean,
    isFetchingMore: Boolean,
    onSelect: (Residence) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A)) // Red: 0.1, Green: 0.1, Blue: 0.1
    ) {
        Text(
            text = "Favorite Places",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        // Assuming your ResidenceListView takes these parameters
        ResidenceListView(
            residences = residences,
            isLoading = isLoading,
            isFetchingMore = isFetchingMore,
            onLoadMore = { /* Implement pagination if needed */ },
            onSelect = onSelect,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FavoriteResidencesPreview() {
    val context = LocalContext.current
    val mockResidences = Residence.mocks(context)

    MaterialTheme {
        FavoriteResidencesContent(
            residences = mockResidences,
            isLoading = false,
            isFetchingMore = false,
            onSelect = { residence -> println("Selected: ${residence.name}") }
        )
    }
}