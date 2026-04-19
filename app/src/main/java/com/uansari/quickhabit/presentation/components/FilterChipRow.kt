package com.uansari.quickhabit.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uansari.quickhabit.domain.model.Filter

@Composable
fun FilterChipRow(
    selectedFilter: Filter,
    allCount: Int,
    pendingCount: Int,
    completedCount: Int,
    onFilterSelected: (Filter) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = selectedFilter == Filter.ALL,
            onClick = { onFilterSelected(Filter.ALL) },
            label = { Text("All ($allCount)") }
        )
        FilterChip(
            selected = selectedFilter == Filter.PENDING,
            onClick = { onFilterSelected(Filter.PENDING) },
            label = { Text("Pending ($pendingCount)") }
        )
        FilterChip(
            selected = selectedFilter == Filter.COMPLETED,
            onClick = { onFilterSelected(Filter.COMPLETED) },
            label = { Text("Done ($completedCount)") }
        )
    }
}