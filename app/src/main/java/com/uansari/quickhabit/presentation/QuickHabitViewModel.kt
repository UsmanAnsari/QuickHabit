package com.uansari.quickhabit.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class QuickHabitViewModel @Inject constructor() : ViewModel(
) {

    private val _uiState = MutableStateFlow(QuickHabitUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffects = Channel<QuickHabitUiEvent>()
    val uiEffects = _uiEffects.receiveAsFlow()

    fun onAction(action: QuickHabitAction) {
        when (action) {
            QuickHabitAction.OnClick -> {
                _uiState.update { it.copy(screenName = "ScreenName Changed") }

                viewModelScope.launch {
                    _uiEffects.send(QuickHabitUiEvent.ShowSnackBar("OnClick Button is called"))
                }
            }
        }
    }

}