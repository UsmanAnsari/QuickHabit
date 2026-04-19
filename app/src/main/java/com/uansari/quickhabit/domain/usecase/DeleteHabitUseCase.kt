package com.uansari.quickhabit.domain.usecase

import com.uansari.quickhabit.domain.model.Habit
import com.uansari.quickhabit.domain.repository.QuickHabitRepository
import javax.inject.Inject

class DeleteHabitUseCase @Inject constructor(
    private val repository: QuickHabitRepository
) {
    suspend operator fun invoke(habit: Habit) {
        repository.removeHabit(habit)
    }
}