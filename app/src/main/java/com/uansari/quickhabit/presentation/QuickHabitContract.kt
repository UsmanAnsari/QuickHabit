package com.uansari.quickhabit.presentation

import com.uansari.quickhabit.domain.model.Filter
import com.uansari.quickhabit.domain.model.HabitWithStreak

data class QuickHabitUiState(
    val allHabits: List<HabitWithStreak> = emptyList(),
    val filteredHabits: List<HabitWithStreak> = emptyList(),
    val selectedFilter: Filter = Filter.ALL,
    val inputText: String = "",
    val selectedEmoji: String = "\uD83D\uDCCC",
    val completedToday: Int = 0,
    val totalHabits: Int = 0,
    val overallProgress: Float = 0f,
    val habitToDelete: HabitWithStreak? = null,
    val isLoading: Boolean = true,
)

sealed interface QuickHabitAction {
    data class AddHabit(val name: String, val emoji: String) : QuickHabitAction
    data class ToggleCompletion(val habitWithStreak: HabitWithStreak) : QuickHabitAction
    data class DeleteHabit(val habitWithStreak: HabitWithStreak) : QuickHabitAction
    data class ConfirmDelete(val habitWithStreak: HabitWithStreak) : QuickHabitAction
    data object DismissDialog : QuickHabitAction
    data class SetFilter(val filter: Filter) : QuickHabitAction
    data class UpdateInput(val inputText: String) : QuickHabitAction
    data class SelectEmoji(val emoji: String) : QuickHabitAction
}

sealed interface QuickHabitUiEvent {
    data class ShowSnackBar(val msg: String) : QuickHabitUiEvent
}