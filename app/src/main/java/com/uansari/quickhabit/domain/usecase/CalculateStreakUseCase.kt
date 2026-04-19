package com.uansari.quickhabit.domain.usecase

import jakarta.inject.Inject
import java.time.LocalDate

class CalculateStreakUseCase @Inject constructor() {
    operator fun invoke(completionDates: List<String>): Pair<Int, Int> {
        if (completionDates.isEmpty()) return Pair(0, 0)

        val dates = completionDates.map { LocalDate.parse(it) }.toSortedSet()

        val currentStreak = calculateCurrentStreak(dates)
        val bestStreak = calculateBestStreak(dates)

        return Pair(currentStreak, bestStreak)
    }

    private fun calculateCurrentStreak(dates: Set<LocalDate>): Int {
        var streak = 0
        var day = LocalDate.now()

        if (!dates.contains(day)) {
            day = day.minusDays(1)
        }

        while (dates.contains(day)) {
            streak++
            day = day.minusDays(1)
        }

        return streak
    }

    private fun calculateBestStreak(dates: Set<LocalDate>): Int {
        if (dates.isEmpty()) return 0

        var bestStreak = 1
        var currentRun = 1
        val sortedDates = dates.sorted()

        for (i in 1 until sortedDates.size) {
            val previous = sortedDates[i - 1]
            val current = sortedDates[i]

            if (current == previous.plusDays(1)) {
                currentRun++
                if (currentRun > bestStreak) bestStreak = currentRun
            } else {
                currentRun = 1
            }
        }

        return bestStreak
    }
}