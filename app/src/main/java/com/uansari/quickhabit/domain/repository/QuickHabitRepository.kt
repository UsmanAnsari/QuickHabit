package com.uansari.quickhabit.domain.repository

import com.uansari.quickhabit.domain.model.Habit
import com.uansari.quickhabit.domain.model.HabitCompletion
import kotlinx.coroutines.flow.Flow

interface QuickHabitRepository {

    suspend fun addHabit(habit: Habit)
    suspend fun removeHabit(habit: Habit)

    suspend fun addHabitCompletion(habitCompletion: HabitCompletion)
    suspend fun removeHabitCompletion(habitCompletion: HabitCompletion)

    fun getAllHabits(): Flow<List<Habit>>

    fun getCompletionsForHabit(habitId: Int): Flow<List<HabitCompletion>>
}