package com.kynarec.subsched.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kynarec.subsched.SubSchedViewModel
import com.kynarec.subsched.SubState
import com.kynarec.subsched.ui.navigation.NavRoutes
import com.kynarec.subsched.ui.screens.home.misc.MessagesCard
import com.kynarec.subsched.ui.screens.home.misc.SubstitutionGrid
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiPaneHomeLayout(
    navController: NavHostController,
    viewModel: SubSchedViewModel = koinViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("SubSched")
                },
                actions = {
                    IconButton(
                        onClick = { navController.navigate(NavRoutes.Settings.Root) }
                    ) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                }
            )
        }
    ) { contentPadding ->
        Box(
            Modifier.fillMaxSize()
                .padding(contentPadding)
        ) {
            val viewModelState = viewModel.state.collectAsStateWithLifecycle()

            val schedule = viewModelState.value as? SubState.Success

            LaunchedEffect(Unit) {
                val oneMinuteInMillis = 60 * 1000
                val timeSinceLastFetch =
                    Clock.System.now().toEpochMilliseconds() - (schedule?.lastFetched ?: 0L)
                if (schedule != null && timeSinceLastFetch < oneMinuteInMillis && !viewModel.refetchPlease) {
                    return@LaunchedEffect
                } else {
                    viewModel.fetchSchedule()
                    viewModel.refetchPlease = false
                }
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

            if (viewModelState.value as? SubState.Error != null) {
                Column(
                    Modifier.fillMaxSize()
                        .align(Alignment.TopCenter),
                    ) {
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

            if (schedule != null) {
                BoxWithConstraints(Modifier.fillMaxSize().padding(16.dp)) {
                    val availableWidth = maxWidth
                    val minDayWidth = 430.dp
                    val spacing = 16.dp

                    val totalSlots = ((availableWidth + spacing) / (minDayWidth + spacing))
                        .toInt()
                        .coerceAtLeast(1)


                    val gridColumns = if (totalSlots > 1) totalSlots - 1 else 0

                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.spacedBy(spacing)
                    ) {
                        if (gridColumns > 0) {
                            val visibleDays = schedule.plan.days.take(gridColumns)
                            visibleDays.forEach { day ->
                                Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                                    SubstitutionGrid(
                                        substitutions = day.substitutions,
                                        date = day.date
                                    )
                                }
                            }
                        }
                        Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                            MessagesCard(schedule.plan.messages)
                        }
                    }
                }
            }
        }
    }
}