package com.uansari.quickhabit.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uansari.quickhabit.domain.model.Filter
import com.uansari.quickhabit.domain.model.HabitWithStreak
import com.uansari.quickhabit.domain.usecase.AddHabitUseCase
import com.uansari.quickhabit.domain.usecase.DeleteHabitUseCase
import com.uansari.quickhabit.domain.usecase.GetHabitsWithStreakUseCase
import com.uansari.quickhabit.domain.usecase.ToggleHabitCompletionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class QuickHabitViewModel @Inject constructor(
    private val addHabitUseCase: AddHabitUseCase,
    private val deleteHabitUseCase: DeleteHabitUseCase,
    private val toggleHabitCompletionUseCase: ToggleHabitCompletionUseCase,
    private val getHabitsWithStreakUseCase: GetHabitsWithStreakUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuickHabitUiState())
    val uiState = _uiState.asStateFlow()
    private val _uiEffects = Channel<QuickHabitUiEvent>(Channel.BUFFERED)
    val uiEffects = _uiEffects.receiveAsFlow()

    init {
        observeHabits()
    }

    fun onAction(action: QuickHabitAction) {
        when (action) {
            is QuickHabitAction.AddHabit -> handleAddHabit(action.name, action.emoji)
            is QuickHabitAction.DeleteHabit -> handleDeleteHabit(action.habitWithStreak)
            is QuickHabitAction.ConfirmDelete -> handleConfirmDelete(action.habitWithStreak)
            QuickHabitAction.DismissDialog -> handleDismissDialog()
            is QuickHabitAction.SelectEmoji -> handleSelectEmoji(action.emoji)
            is QuickHabitAction.SetFilter -> handleSetFilter(action.filter)
            is QuickHabitAction.ToggleCompletion -> handleToggleCompletion(action.habitWithStreak)
            is QuickHabitAction.UpdateInput -> handleUpdateInput(action.inputText)
        }
    }

    private fun observeHabits() {
        viewModelScope.launch {
            getHabitsWithStreakUseCase().collect { habits ->
                val currentFilter = _uiState.value.selectedFilter
                val completedToday = habits.count { it.isCompletedToday }
                val total = habits.size

                _uiState.update { state ->
                    state.copy(
                        allHabits = habits,
                        filteredHabits = applyFilter(habits, currentFilter),
                        completedToday = completedToday,
                        totalHabits = total,
                        overallProgress = calculateOverallProgress(total, completedToday),
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun handleAddHabit(name: String, emoji: String) {
        viewModelScope.launch {
            addHabitUseCase(name, emoji)
            _uiState.update {
                it.copy(
                    inputText = "", selectedEmoji = "📌"
                )
            }
        }
    }

    private fun handleToggleCompletion(habitWithStreak: HabitWithStreak) {
        viewModelScope.launch {
            toggleHabitCompletionUseCase(
                habitId = habitWithStreak.habit.id, completions = habitWithStreak.completions
            )
        }
    }

    private fun handleDeleteHabit(habitWithStreak: HabitWithStreak) {
        _uiState.update { it.copy(habitToDelete = habitWithStreak) }
    }

    private fun handleConfirmDelete(habitWithStreak: HabitWithStreak) {
        viewModelScope.launch {
            deleteHabitUseCase(habitWithStreak.habit)
            _uiState.update { it.copy(habitToDelete = null) }
            _uiEffects.send(
                QuickHabitUiEvent.ShowSnackBar("${habitWithStreak.habit.emoji} ${habitWithStreak.habit.name} deleted")
            )
        }
    }

    private fun handleDismissDialog() {
        _uiState.update { it.copy(habitToDelete = null) }
    }

    private fun handleSetFilter(filter: Filter) {
        _uiState.update { state ->
            state.copy(
                selectedFilter = filter, filteredHabits = applyFilter(state.allHabits, filter)
            )
        }
    }

    private fun handleUpdateInput(text: String) {
        _uiState.update { it.copy(inputText = text) }
    }

    private fun handleSelectEmoji(emoji: String) {
        _uiState.update { it.copy(selectedEmoji = emoji) }
    }

    private fun applyFilter(
        habits: List<HabitWithStreak>, filter: Filter
    ): List<HabitWithStreak> = when (filter) {
        Filter.ALL -> habits
        Filter.PENDING -> habits.filter { !it.isCompletedToday }
        Filter.COMPLETED -> habits.filter { it.isCompletedToday }
    }

    private fun calculateOverallProgress(
        total: Int, completedToday: Int
    ): Float = if (total == 0) 0f
    else (completedToday.toFloat() / total.toFloat()).coerceIn(0f, 1f)
}