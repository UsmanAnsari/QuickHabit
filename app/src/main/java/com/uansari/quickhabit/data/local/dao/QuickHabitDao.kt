package com.uansari.quickhabit.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.uansari.quickhabit.data.local.model.HabitCompletionEntity
import com.uansari.quickhabit.data.local.model.HabitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuickHabitDao {
    @Query("SELECT * FROM habits ORDER BY created_at ASC")
    fun getAllHabits(): Flow<List<HabitEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun insertOrUpdateHabit(habitEntity: HabitEntity)

    @Delete
    suspend fun deleteHabit(habitEntity: HabitEntity)

    @Query("SELECT * FROM habit_completions WHERE habit_id=:habitId")
    fun getCompletionsForHabit(habitId: Int): Flow<List<HabitCompletionEntity>>

    @Insert(onConflict = IGNORE)
    suspend fun insertHabitCompletion(habitCompletionEntity: HabitCompletionEntity)

    @Delete
    suspend fun deleteHabitCompletion(habitCompletionEntity: HabitCompletionEntity)
}