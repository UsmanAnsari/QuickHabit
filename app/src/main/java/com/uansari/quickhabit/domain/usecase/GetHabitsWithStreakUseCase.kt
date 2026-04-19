package com.uansari.quickhabit.domain.usecase

import com.uansari.quickhabit.domain.model.HabitWithStreak
import com.uansari.quickhabit.domain.repository.QuickHabitRepository
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate

class GetHabitsWithStreakUseCase @Inject constructor(
    private val repository: QuickHabitRepository,
    private val calculateStreak: CalculateStreakUseCase,
    private val buildWeeklyGrid: BuildWeeklyGridUseCase
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<List<HabitWithStreak>> {
        return repository.getAllHabits().flatMapLatest { habits ->
            if (habits.isEmpty()) return@flatMapLatest flowOf(emptyList())

            val habitWithStreakFlows = habits.map { habit ->
                repository.getCompletionsForHabit(habit.id)
                    .combine(flowOf(habit)) { completions, h ->
                        val today = LocalDate.now().toString()
                        val dates = completions.map { it.completedDate }
                        val (currentStreak, bestStreak) = calculateStreak(dates)

                        HabitWithStreak(
                            habit = h,
                            isCompletedToday = completions.any {
                                it.completedDate == today
                            },
                            currentStreak = currentStreak,
                            bestStreak = bestStreak,
                            weeklyGrid = buildWeeklyGrid(dates),
                            completions = completions
                        )
                    }
            }

            combine(habitWithStreakFlows) { it.toList() }
        }
    }
}
