package com.kynarec.subsched.ui.screens.tomorrow

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kynarec.subsched.SubSchedViewModel
import com.kynarec.subsched.SubState
import com.kynarec.subsched.ui.screens.home.misc.SubstitutionGrid
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock

@Composable
fun TomorrowScreen(
    viewModel: SubSchedViewModel = koinViewModel(),
    snackBarHostState: SnackbarHostState
) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        val viewModelState = viewModel.state.collectAsStateWithLifecycle()
        val viewModelLastSuccessfulState = viewModel.lastSuccessfulFetch.collectAsStateWithLifecycle()

        val schedule = viewModelLastSuccessfulState.value as? SubState.Success

        LaunchedEffect(Unit) {
            val oneMinuteInMillis = 60 * 1000
            val timeSinceLastFetch = Clock.System.now().toEpochMilliseconds() - (schedule?.lastFetched ?: 0L)
            if (schedule != null && timeSinceLastFetch < oneMinuteInMillis && !viewModel.refetchPlease) {
                return@LaunchedEffect
            } else {
                viewModel.fetchSchedule()
                viewModel.refetchPlease = false
            }
        }

        LaunchedEffect(viewModelState.value) {
            if (viewModelState.value as? SubState.Error != null) {
                snackBarHostState.showSnackbar("Error fetching, using last successful fetch")
                println("Error fetching, using last successful fetch")
            }
        }


        if (viewModelLastSuccessfulState.value == SubState.Loading) {
            Row(
                Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                CircularProgressIndicator()
            }
        }

        if (schedule != null) {
            val today = schedule.plan.days[1]
            SubstitutionGrid(today.substitutions, date = today.date)
        }

        if (viewModelState.value as? SubState.Error != null) {
            if (viewModelLastSuccessfulState.value as? SubState.Success != null) {
                println("using last successful fetch")
                val today = (viewModelLastSuccessfulState.value as SubState.Success).plan.days.first()
                SubstitutionGrid(today.substitutions, date = today.date)
            } else {
                Column(
                    Modifier.fillMaxSize()
                ) {
                    Spacer(Modifier.height(8.dp))
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            "Error fetching",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            "Maybe credentials are empty",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}