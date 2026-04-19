package com.uansari.quickhabit.domain.model

data class HabitWithStreak(
    val habit: Habit,
    val isCompletedToday: Boolean,
    val currentStreak: Int,
    val bestStreak: Int,
    val weeklyGrid: List<WeekDay>,
    val completions: List<HabitCompletion>
)
