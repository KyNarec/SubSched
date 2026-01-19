package com.kynarec.subsched.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.kynarec.shared.data.models.Substitution
import com.kynarec.subsched.SubSchedViewModel
import com.kynarec.subsched.SubState
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock

@Composable
fun HomeScreen(
    viewModel: SubSchedViewModel = koinViewModel(),
    navController: NavController
) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        val viewModelState = viewModel.state.collectAsStateWithLifecycle()

        val schedule = viewModelState.value as? SubState.Success

        LaunchedEffect(Unit) {
            val oneMinuteInMillis = 60 * 1000
            val timeSinceLastFetch = Clock.System.now().toEpochMilliseconds() - (schedule?.lastFetched ?: 0L)
            if (schedule != null && timeSinceLastFetch < oneMinuteInMillis) {
                return@LaunchedEffect
            } else {
                viewModel.fetchSchedule()
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

        if (schedule != null) {
            val today = schedule.plan.days[2]
            SubstitutionGrid(today.substitutions, date = today.date)
//            today.substitutions.forEachIndexed { index, substitution ->
//                println("=== $index ===")
//                println(substitution.coveringTeacher.name)
//                println(substitution.lesson)
//                println(substitution.className)
//                println(substitution.absentTeacher.name)
//                println(substitution.subject)
//                println(substitution.room)
//                println(substitution.info)
//            }
        }
    }
}

@Composable
fun SubstitutionGrid(substitutions: List<Substitution>, date: String) {
    SelectionContainer {
        Card(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // --- Table Header ---
                Text(date, style = MaterialTheme.typography.titleMedium)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(vertical = 12.dp, horizontal = 8.dp)
                ) {
                    HeaderText("Std.", Modifier.weight(0.6f))
                    HeaderText("Kl.", Modifier.weight(0.8f))
                    HeaderText("Ver.", Modifier.weight(1.2f))
                    HeaderText("Raum", Modifier.weight(0.8f))
                    HeaderText("Info", Modifier.weight(1.5f))
                }

                // --- Rows ---
                LazyColumn {
                    items(substitutions) { item ->
                        SubstitutionRow(item)
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outlineVariant.copy(
                                alpha = 0.5f
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SubstitutionRow(item: Substitution) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Std. (Lesson)
        Text(
            text = "${item.lesson}.",
            modifier = Modifier.weight(0.6f),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )

        // Kl. (ClassName)
        Text(item.className, Modifier.weight(0.8f), style = MaterialTheme.typography.bodyMedium)

        // Ver. (The Teacher logic: Show who is covering)
        Column(Modifier.weight(1.2f)) {
            Text(item.subject, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
            Text(
                text = "${item.absentTeacher.name} ➔ ${item.coveringTeacher.name}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }

        // Raum (Room)
        Text(item.room, Modifier.weight(0.8f), style = MaterialTheme.typography.bodyMedium)

        // Info
        Text(
            text = item.info,
            modifier = Modifier.weight(1.5f),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun HeaderText(text: String, modifier: Modifier) {
    Text(
        text = text,
        modifier = modifier,
        color = MaterialTheme.colorScheme.onPrimary,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.ExtraBold
    )
}