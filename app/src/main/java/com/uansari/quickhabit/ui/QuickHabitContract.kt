package com.uansari.quickhabit.ui

data class QuickHabitUiState(
    val screenName: String = "Hello World"
)

sealed interface QuickHabitAction {
    data object OnClick : QuickHabitAction
}

sealed interface QuickHabitUiEvent {
    data class ShowSnackBar(val msg: String) : QuickHabitUiEvent
}