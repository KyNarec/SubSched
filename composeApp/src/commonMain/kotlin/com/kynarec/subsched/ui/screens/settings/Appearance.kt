package com.kynarec.subsched.ui.screens.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.kynarec.subsched.DARK_THEME_KEY
import com.kynarec.subsched.SubSchedViewModel
import com.kynarec.subsched.ui.navigation.TransitionEffect
import com.kynarec.subsched.ui.screens.settings.misc.SettingComponentEnumChoice
import com.kynarec.subsched.ui.screens.settings.misc.SettingComponentSwitch
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Appearance(
    viewModel: SubSchedViewModel = koinViewModel(),
    navController: NavController,
    backNavigation: () -> Unit = { navController.popBackStack() }
) {
    val scope = rememberCoroutineScope()
    val transitionEffectFlow by viewModel.transitionEffectFlow.collectAsStateWithLifecycle(viewModel.transitionEffect)


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
                .padding(contentPadding),
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
                                scope.launch {
                                    viewModel.putBoolean(DARK_THEME_KEY, it)
                                    viewModel.darkThemeDefault = it
                                }
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
                                scope.launch { viewModel.putTransitionEffect(it) }
                            },
                            labelMapper = { it.label }
                        )
                    }
                }
            }
        }
    }
}