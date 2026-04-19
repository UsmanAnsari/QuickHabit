package com.uansari.quickhabit.di

import android.content.Context
import androidx.room.Room
import com.uansari.quickhabit.data.local.dao.QuickHabitDao
import com.uansari.quickhabit.data.local.database.QuickHabitDataBase
import com.uansari.quickhabit.data.repository.QuickHabitRepositoryImpl
import com.uansari.quickhabit.domain.repository.QuickHabitRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class QuickHabitModule {

    @Binds
    @Singleton
    abstract fun bindInterfaces(habitRepositoryImpl: QuickHabitRepositoryImpl): QuickHabitRepository

    companion object {

        @Provides
        @Singleton
        fun provideDatabase(
            @ApplicationContext context: Context
        ): QuickHabitDataBase = Room.databaseBuilder(
            context,
            QuickHabitDataBase::class.java,
            "quick_habit_db",
        ).fallbackToDestructiveMigration(true).build()

        @Provides
        @Singleton
        fun provideDao(
            quickHabitDataBase: QuickHabitDataBase
        ): QuickHabitDao = quickHabitDataBase.quickHabitDao()
    }
}