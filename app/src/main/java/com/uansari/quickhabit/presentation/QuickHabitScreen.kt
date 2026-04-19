package com.uansari.quickhabit.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uansari.quickhabit.presentation.components.AddHabitInput
import com.uansari.quickhabit.presentation.components.DeleteConfirmDialog
import com.uansari.quickhabit.presentation.components.EmojiPicker
import com.uansari.quickhabit.presentation.components.EmptyHabitsState
import com.uansari.quickhabit.presentation.components.FilterChipRow
import com.uansari.quickhabit.presentation.components.HabitItem
import com.uansari.quickhabit.presentation.components.OverallProgressBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showSystemUi = true)
fun QuickHabitScreen(
    quickHabitViewModel: QuickHabitViewModel = hiltViewModel()
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val uiState by quickHabitViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        quickHabitViewModel.uiEffects.collect {
            when (it) {
                is QuickHabitUiEvent.ShowSnackBar -> {
                    snackbarHostState.showSnackbar(it.msg)
                }
            }
        }
    }

    uiState.habitToDelete?.let { habit ->
        DeleteConfirmDialog(habitWithStreak = habit, onConfirm = {
            quickHabitViewModel.onAction(QuickHabitAction.ConfirmDelete(habit))
        }, onDismiss = {
            quickHabitViewModel.onAction(QuickHabitAction.DismissDialog)
        })
    }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = { Text("QuickHabit") })
    }, snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OverallProgressBar(
                completedToday = uiState.completedToday,
                totalHabits = uiState.totalHabits,
                progress = uiState.overallProgress
            )

            AddHabitInput(value = uiState.inputText, onValueChange = {
                quickHabitViewModel.onAction(QuickHabitAction.UpdateInput(it))
            }, onAdd = {
                quickHabitViewModel.onAction(
                    QuickHabitAction.AddHabit(uiState.inputText, uiState.selectedEmoji)
                )
            })

            EmojiPicker(
                selectedEmoji = uiState.selectedEmoji, onEmojiSelected = {
                    quickHabitViewModel.onAction(QuickHabitAction.SelectEmoji(it))
                })

            FilterChipRow(
                selectedFilter = uiState.selectedFilter,
                allCount = uiState.allHabits.size,
                pendingCount = uiState.allHabits.count { !it.isCompletedToday },
                completedCount = uiState.allHabits.count { it.isCompletedToday },
                onFilterSelected = {
                    quickHabitViewModel.onAction(QuickHabitAction.SetFilter(it))
                })

            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                uiState.filteredHabits.isEmpty() -> {
                    EmptyHabitsState(
                        filter = uiState.selectedFilter, modifier = Modifier.weight(1f)
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = uiState.filteredHabits,
                            key = { it.habit.id }) { habitWithStreak ->
                            HabitItem(habitWithStreak = habitWithStreak, onToggle = {
                                quickHabitViewModel.onAction(
                                    QuickHabitAction.ToggleCompletion(habitWithStreak)
                                )
                            }, onDelete = {
                                quickHabitViewModel.onAction(
                                    QuickHabitAction.DeleteHabit(habitWithStreak)
                                )
                            })
                        }
                    }
                }
            }
        }
    }
}