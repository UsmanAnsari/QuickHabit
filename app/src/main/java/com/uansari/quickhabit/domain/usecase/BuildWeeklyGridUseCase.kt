package com.uansari.quickhabit.domain.usecase

import com.uansari.quickhabit.domain.model.WeekDay
import jakarta.inject.Inject
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

class BuildWeeklyGridUseCase @Inject constructor() {

    operator fun invoke(completionDates: List<String>): List<WeekDay> {
        val today = LocalDate.now()
        val completionSet = completionDates.toSet()

        return (6 downTo 0).map { daysAgo ->
            val date = today.minusDays(daysAgo.toLong())
            val dateString = date.toString()

            WeekDay(
                label = date.dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.getDefault()),
                date = dateString,
                isCompleted = completionSet.contains(dateString)
            )
        }
    }
}
