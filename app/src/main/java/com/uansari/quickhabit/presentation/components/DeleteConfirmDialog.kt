package com.uansari.quickhabit.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.uansari.quickhabit.domain.model.HabitWithStreak

@Composable
fun DeleteConfirmDialog(
    habitWithStreak: HabitWithStreak,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Habit") },
        text = {
            Text(
                "Are you sure you want to delete " +
                        "${habitWithStreak.habit.emoji} ${habitWithStreak.habit.name}? " +
                        "All completion history will be lost."
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Delete", color = androidx.compose.material3.MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}