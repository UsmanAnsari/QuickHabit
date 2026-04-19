package com.uansari.quickhabit.data.repository

import com.uansari.quickhabit.data.local.dao.QuickHabitDao
import com.uansari.quickhabit.data.local.model.HabitCompletionEntity
import com.uansari.quickhabit.data.local.model.HabitEntity
import com.uansari.quickhabit.domain.model.Habit
import com.uansari.quickhabit.domain.model.HabitCompletion
import com.uansari.quickhabit.domain.repository.QuickHabitRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class QuickHabitRepositoryImpl @Inject constructor(private val quickHabitDao: QuickHabitDao) :
    QuickHabitRepository {
    override suspend fun addHabit(habit: Habit) {
        quickHabitDao.insertOrUpdateHabit(habit.toEntity())
    }

    override suspend fun removeHabit(habit: Habit) {
        quickHabitDao.deleteHabit(habit.toEntity())
    }

    override suspend fun addHabitCompletion(habitCompletion: HabitCompletion) {

        quickHabitDao.insertHabitCompletion(habitCompletion.toEntity())
    }

    override suspend fun removeHabitCompletion(habitCompletion: HabitCompletion) {
        quickHabitDao.deleteHabitCompletion(habitCompletion.toEntity())
    }

    override fun getAllHabits(): Flow<List<Habit>> {
        return quickHabitDao.getAllHabits().map { list -> list.map { it.toDomain() } }
    }

    override fun getCompletionsForHabit(habitId: Int): Flow<List<HabitCompletion>> {
        return quickHabitDao.getCompletionsForHabit(habitId)
            .map { list -> list.map { it.toDomain() } }
    }

    private fun HabitEntity.toDomain(): Habit {
        return Habit(
            id = id,
            name = name,
            emoji = emoji,
            createdAt = createdAt,
        )
    }

    private fun Habit.toEntity(): HabitEntity {
        return HabitEntity(
            id = id,
            name = name,
            emoji = emoji,
            createdAt = createdAt,
        )
    }

    private fun HabitCompletionEntity.toDomain(): HabitCompletion {
        return HabitCompletion(
            id = id,
            habitId = habitId,
            completedDate = completedDate,
        )
    }

    private fun HabitCompletion.toEntity(): HabitCompletionEntity {
        return HabitCompletionEntity(
            id = id,
            habitId = habitId,
            completedDate = completedDate,
        )
    }
}