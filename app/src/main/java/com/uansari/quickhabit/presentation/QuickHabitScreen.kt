package com.uansari.quickhabit.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showSystemUi = true)
fun QuickHabitScreen(
    quickHabitViewModel: QuickHabitViewModel = hiltViewModel()
) {
    val snackBarHostState = SnackbarHostState()
    val uiState = quickHabitViewModel.uiState.collectAsStateWithLifecycle()


    LaunchedEffect(Unit) {
        quickHabitViewModel.uiEffects.collect {
            when (it) {
                is QuickHabitUiEvent.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(it.msg)
                }
            }
        }
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        },
        topBar = {
            CenterAlignedTopAppBar({
                Text("QuickHabit")
            })
        }, modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DisplayName(name = uiState.value.screenName) {
                quickHabitViewModel.onAction(QuickHabitAction.OnClick)
            }
        }
    }
}


@Composable
fun DisplayName(name: String, onClick: () -> Unit) {
    Text(
        text = name,
        style = MaterialTheme.typography.displaySmall,
        modifier = Modifier.clickable(onClick = onClick)
    )
}