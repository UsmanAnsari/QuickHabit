package com.uansari.quickhabit.domain.model

data class HabitCompletion(
    val id: Int = 0,
    val habitId: Int,
    val completedDate: String,
)
