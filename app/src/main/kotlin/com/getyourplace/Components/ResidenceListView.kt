package com.getyourplace.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.getyourplace.Models.Residence

@Composable
fun ResidenceListView(
    residences: List<Residence>,
    isLoading: Boolean,
    isFetchingMore: Boolean,
    isScrollable: Boolean = true,
    onLoadMore: () -> Unit,
    onSelect: (Residence) -> Unit,
    modifier: Modifier = Modifier
) {
    // If isScrollable is false, we use a Column with ForEach (rare in Android)
    // Otherwise, we use the standard LazyColumn
    if (!isScrollable) {
        Column(
            modifier = modifier.padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ListViewContent(residences, isLoading, isFetchingMore, onLoadMore, onSelect)
        }
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (isLoading && residences.isEmpty()) {
                items(3) { ResidenceSkeleton() }
            } else {
                itemsIndexed(residences) { index, residence ->
                    // Trigger onLoadMore when the last item appears
                    LaunchedEffect(residences.size) {
                        if (index == residences.size - 1) {
                            onLoadMore()
                        }
                    }

                    // Assuming ResidenceView is a separate Composable you created
                    ResidenceView(
                        residence = residence,
                        onTap = { onSelect(residence) }
                    )
                }

                if (isFetchingMore) {
                    items(2) { ResidenceSkeleton() }
                }
            }
        }
    }
}

/**
 * Helper for the non-scrollable version (equivalent to ForEach in SwiftUI)
 */
@Composable
private fun ListViewContent(
    residences: List<Residence>,
    isLoading: Boolean,
    isFetchingMore: Boolean,
    onLoadMore: () -> Unit,
    onSelect: (Residence) -> Unit
) {
    if (isLoading && residences.isEmpty()) {
        repeat(3) { ResidenceSkeleton() }
    } else {
        residences.forEachIndexed { index, residence ->
            ResidenceView(residence = residence, onTap = { onSelect(residence) })
            if (index == residences.lastIndex) {
                LaunchedEffect(Unit) { onLoadMore() }
            }
        }
        if (isFetchingMore) {
            repeat(2) { ResidenceSkeleton() }
        }
    }
}

@Composable
fun ResidenceSkeleton() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Image Placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Gray.copy(alpha = 0.3f))
        )
        // Title Placeholder
        Box(
            modifier = Modifier
                .width(150.dp)
                .height(20.dp)
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.Gray.copy(alpha = 0.3f))
        )
    }
}