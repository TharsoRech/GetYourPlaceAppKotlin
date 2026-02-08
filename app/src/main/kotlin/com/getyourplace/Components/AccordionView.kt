package com.getyourplace.Components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AccordionView(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    // Equivalent to @State var isAccordionExpanded
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .padding(top = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black, shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            // Header (The Label part of DisclosureGroup)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f)
                )

                // The chevron icon that rotates/changes based on state
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = Color.White
                )
            }

            // Expanded Content (Equivalent to the DisclosureGroup trailing closure)
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                // Equivalent to ScrollView with maxHeight
                Box(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .heightIn(max = 500.dp) // Equivalent to maxHeight: 500
                        .verticalScroll(rememberScrollState())
                ) {
                    content()
                }
            }
        }
    }
}