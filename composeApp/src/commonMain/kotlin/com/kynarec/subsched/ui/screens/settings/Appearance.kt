package com.kynarec.subsched.ui.screens.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.KeyboardDoubleArrowDown
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.kynarec.subsched.DARK_THEME_KEY
import com.kynarec.subsched.DEFAULT_CARD_SIZE
import com.kynarec.subsched.DEFAULT_REFRESH_INTERVAL
import com.kynarec.subsched.SubSchedViewModel
import com.kynarec.subsched.ui.navigation.TransitionEffect
import com.kynarec.subsched.ui.screens.settings.misc.SettingComponentEnumChoice
import com.kynarec.subsched.ui.screens.settings.misc.SettingComponentSwitch
import com.kynarec.subsched.util.CardSize
import com.kynarec.subsched.util.WindowHandler
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Appearance(
    viewModel: SubSchedViewModel = koinViewModel(),
    navController: NavController,
    windowHandler: WindowHandler? = null,
    backNavigation: () -> Unit = { navController.popBackStack() }
) {
    val scope = rememberCoroutineScope()
    val transitionEffectFlow by viewModel.transitionEffectFlow.collectAsStateWithLifecycle(viewModel.transitionEffect)
    val refreshInterval by viewModel.refreshInterval.collectAsStateWithLifecycle(
        DEFAULT_REFRESH_INTERVAL
    )
    val cardSize by viewModel.cardSizeFlow.collectAsStateWithLifecycle(DEFAULT_CARD_SIZE)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Appearance", maxLines = 1, overflow = TextOverflow.Ellipsis) },
                navigationIcon = {
                    IconButton(onClick = { backNavigation() }) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { contentPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(bottom = 8.dp),
            contentAlignment = Alignment.TopCenter
        )
        {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                item {
                    ElevatedCard {
                        SettingComponentSwitch(
                            icon = Icons.Default.DarkMode,
                            title = "Dark theme",
                            description = "Use dark theme",
                            onCheckedChange = {
//                                scope.launch {
                                viewModel.putBoolean(DARK_THEME_KEY, it)
                                viewModel.darkThemeDefault = it
//                                }
                            },
                            checked = viewModel.darkThemeDefault
                        )
                    }
                }
                item {
                    Spacer(Modifier.height(16.dp))
                }
                item {
                    ElevatedCard {
                        SettingComponentEnumChoice(
                            icon = Icons.Default.Animation,
                            title = "Screen Transition Effect",
                            description = "Choose how screens transition in the app",
                            enumValues = TransitionEffect.entries,
                            selected = transitionEffectFlow,
                            onValueSelected = {
                                viewModel.putTransitionEffect(it)
//                                scope.launch { viewModel.putTransitionEffect(it) }
                            },
                            labelMapper = { it.label }
                        )
                    }
                }
                item {
                    Spacer(Modifier.height(16.dp))
                }
                item {
                    ElevatedCard {
                        SettingComponentSwitch(
                            icon = Icons.Default.KeyboardDoubleArrowDown,
                            title = "Auto scroll",
                            description = "Automatically scroll through the schedule",
                            onCheckedChange = {
                                viewModel.autoScroll = it
                            },
                            checked = viewModel.autoScroll,
                        )
                    }
                }

                windowHandler?.let { state ->
                    item {
                        Spacer(Modifier.height(16.dp))
                    }
                    item {
                        ElevatedCard {
                            SettingComponentSwitch(
                                icon = Icons.Default.Fullscreen,
                                title = "Fullscreen",
                                description = "Toggle fullscreen (tip: press F11 to toggle)",
                                onCheckedChange = {
                                    scope.launch {
                                        state.toggleFullscreen()
                                    }
                                },
                                checked = state.isFullscreen,
                            )
                        }
                    }
                }

                item {
                    Spacer(Modifier.height(16.dp))
                }
                item {
                    ElevatedCard {
                        val options = listOf(60, 120, 300, 600, 1800, 3600) // in seconds
                        var slidingIndex by remember { mutableFloatStateOf(refreshInterval.toFloat()) }
                        var isSliding by remember { mutableStateOf(false) }

                        val selectedIndex = when {
                            isSliding -> slidingIndex
                            else -> options.indexOf(refreshInterval).toFloat()
                        }
                        Column {
                            Text(
                                "Auto refresh interval",
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(16.dp)
                            )
                            Slider(
                                value = selectedIndex,
                                onValueChange = {
                                    isSliding = true
                                    slidingIndex = it
                                },
                                onValueChangeFinished = {
                                    scope.launch {
                                        viewModel.updateRefreshInterval(options[slidingIndex.toInt()])
                                        isSliding = false
                                    }
                                },
                                valueRange = 0f..(options.size - 1).toFloat(),
                                steps = options.size - 2,
                                modifier = Modifier.padding(
                                    start = 16.dp,
                                    end = 16.dp,
                                    bottom = 16.dp
                                ),
                            )

                            Text(
                                "Interval: ${options[selectedIndex.toInt()].formatDuration()}",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(
                                    start = 16.dp,
                                    end = 16.dp,
                                    bottom = 16.dp
                                )
                                    .align(Alignment.Start)
                            )
                        }
                    }
                }
                item {
                    Spacer(Modifier.height(16.dp))
                }
                item {
                    ElevatedCard {
                        val options = CardSize.entries
                        var slidingIndex by remember { mutableFloatStateOf(options.indexOf(cardSize).toFloat()) }
                        var isSliding by remember { mutableStateOf(false) }

                        val selectedIndex = when {
                            isSliding -> slidingIndex
                            else -> options.indexOf(cardSize).toFloat()
                        }

                        Column {
                            Text(
                                "Card size",
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(16.dp)
                            )

                            Slider(
                                value = selectedIndex,
                                onValueChange = {
                                    isSliding = true
                                    slidingIndex = it
                                },
                                onValueChangeFinished = {
                                    scope.launch {
                                        viewModel.putCardSize(
                                            options[slidingIndex.toInt()]
                                        )
                                        isSliding = false
                                    }
                                },
                                valueRange = 0f..(options.size - 1).toFloat(),
                                steps = options.size - 2,
                                modifier = Modifier.padding(
                                    start = 16.dp,
                                    end = 16.dp,
                                    bottom = 16.dp
                                ),
                            )
                        }
                    }

                }
            }
        }
    }
}

fun Int.formatDuration(): String {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    return when {
        hours > 0 -> "$hours hour${if (hours > 1) "s" else ""} ${if (minutes != 0) "$minutes minutes" else ""}"
        else -> "$minutes minutes"
    }
}