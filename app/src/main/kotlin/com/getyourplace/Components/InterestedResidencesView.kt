package com.getyourplace.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.getyourplace.Components.ViewModels.InterestedResidencesViewModel
import com.getyourplace.Models.Residence

@Composable
fun InterestedResidencesView(
    onSelect: (Residence) -> Unit,
    viewModel: InterestedResidencesViewModel = viewModel()
) {
    InterestedResidencesContent(
        residences = viewModel.interestedResidences,
        isLoading = viewModel.isLoading,
        isFetchingMore = viewModel.isFetchingMore,
        onSelect = onSelect
    )
}

@Composable
fun InterestedResidencesContent(
    residences: List<Residence>,
    isLoading: Boolean,
    isFetchingMore: Boolean,
    onSelect: (Residence) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A)), // Equivalent to Color(red: 0.1, green: 0.1, blue: 0.1)
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Interested Places",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(top = 10.dp)
        )

        // Using the ResidenceListView we converted earlier
        ResidenceListView(
            residences = residences,
            isLoading = isLoading,
            isFetchingMore = isFetchingMore,
            onLoadMore = { /* Pagination logic if needed */ },
            onSelect = onSelect,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InterestedResidencesPreview() {
    val context = LocalContext.current
    // Use your existing mock helper!
    val mockData = Residence.mocks(context)

    androidx.compose.material3.MaterialTheme {
        InterestedResidencesContent(
            residences = mockData,
            isLoading = false,
            isFetchingMore = false,
            onSelect = { residence ->
                println("Preview selected: ${residence.name}")
            }
        )
    }
}