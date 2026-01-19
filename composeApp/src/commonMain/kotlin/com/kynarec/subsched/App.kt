package com.kynarec.subsched

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.kynarec.shared.SubSchedRepository
import com.kynarec.subsched.ui.Navigation
import com.kynarec.subsched.ui.theme.SubSchedTheme
import org.koin.compose.viewmodel.koinViewModel

import subsched.composeapp.generated.resources.Res
import subsched.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    SubSchedTheme(

    ) {
        val viewModel: SubSchedViewModel = koinViewModel()
        val uiState by viewModel.state.collectAsState()

        val navController = rememberNavController()

        LaunchedEffect(Unit) {
            viewModel.fetchSchedule()
        }

        when (val currentState = uiState) {
            is SubState.Loading -> {
//                LoadingView()
            }
            is SubState.Success -> {
//                ScheduleContent(plan = currentState.plan)
            }
            is SubState.Error -> {
//                ErrorView(message = currentState.message)
            }
        }
        Navigation(navController)

    }
}