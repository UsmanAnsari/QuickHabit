package com.uansari.quickhabit.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.uansari.quickhabit.data.local.dao.QuickHabitDao
import com.uansari.quickhabit.data.local.model.HabitCompletionEntity
import com.uansari.quickhabit.data.local.model.HabitEntity

@Database(
    entities = [HabitEntity::class, HabitCompletionEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class QuickHabitDataBase : RoomDatabase() {
    abstract fun quickHabitDao(): QuickHabitDao
}