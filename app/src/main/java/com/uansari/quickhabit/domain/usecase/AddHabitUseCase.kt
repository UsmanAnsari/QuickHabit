package com.uansari.quickhabit.domain.usecase

import com.uansari.quickhabit.domain.model.Habit
import com.uansari.quickhabit.domain.repository.QuickHabitRepository
import javax.inject.Inject

class AddHabitUseCase @Inject constructor(
    private val repository: QuickHabitRepository
) {
    suspend operator fun invoke(name: String, emoji: String) {
        if (name.isBlank() || emoji.isBlank()) return
        repository.addHabit(
            Habit(
                name = name.trim(),
                emoji = emoji,
                createdAt = System.currentTimeMillis(),
            )
        )
    }
}