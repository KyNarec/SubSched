package com.kynarec.subsched.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.kynarec.subsched.SubSchedViewModel
import com.kynarec.subsched.SubState
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    viewModel: SubSchedViewModel = koinViewModel(),
    navController: NavController
) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val viewModelState = viewModel.state.collectAsStateWithLifecycle()

        val schedule = viewModelState.value as? SubState.Success

        LaunchedEffect(Unit) {
            viewModel.fetchSchedule()
        }


        if (viewModelState.value == SubState.Loading) {
            Row(
                Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                CircularProgressIndicator()
            }
        }
    }
}