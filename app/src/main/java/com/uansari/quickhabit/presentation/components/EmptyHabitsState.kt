package com.uansari.quickhabit.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.uansari.quickhabit.domain.model.Filter

@Composable
fun EmptyHabitsState(
    filter: Filter,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = when (filter) {
                Filter.ALL -> "🌱"
                Filter.PENDING -> "🎉"
                Filter.COMPLETED -> "💤"
            },
            style = MaterialTheme.typography.displayMedium
        )
        Text(
            text = when (filter) {
                Filter.ALL -> "No habits yet"
                Filter.PENDING -> "All done for today!"
                Filter.COMPLETED -> "Nothing completed yet"
            },
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = when (filter) {
                Filter.ALL -> "Add a habit above to get started"
                Filter.PENDING -> "Great work — come back tomorrow"
                Filter.COMPLETED -> "Mark a habit done to see it here"
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}