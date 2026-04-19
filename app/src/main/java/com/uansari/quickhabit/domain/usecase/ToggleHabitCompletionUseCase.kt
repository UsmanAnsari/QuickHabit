package com.uansari.quickhabit.domain.usecase

import com.uansari.quickhabit.domain.model.HabitCompletion
import com.uansari.quickhabit.domain.repository.QuickHabitRepository
import java.time.LocalDate
import javax.inject.Inject

class ToggleHabitCompletionUseCase @Inject constructor(
    private val repository: QuickHabitRepository
) {
    suspend operator fun invoke(
        habitId: Int,
        completions: List<HabitCompletion>,
    ) {

        val today = LocalDate.now().toString()
        val todayCompletion = completions.find { it.completedDate == today }

        if (todayCompletion != null) {
            repository.removeHabitCompletion(todayCompletion)
        } else {
            repository.addHabitCompletion(
                HabitCompletion(
                    habitId = habitId,
                    completedDate = today,
                )
            )
        }
    }
}