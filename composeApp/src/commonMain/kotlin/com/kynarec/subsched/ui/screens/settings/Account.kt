package com.kynarec.subsched.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.kynarec.subsched.SubSchedViewModel
import com.kynarec.subsched.SubState
import com.kynarec.subsched.ui.screens.settings.misc.SettingComponentSwitch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Account(
    viewModel: SubSchedViewModel = koinViewModel(),
    navController: NavController,
    backNavigation: () -> Unit = { navController.popBackStack() }
) {
    val viewModelState = viewModel.state.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    val confirmButtonClicked = remember { mutableStateOf(false) }
    val isCheckingCredentials = remember { mutableStateOf(false) }


    LaunchedEffect(viewModelState.value) {
        if (confirmButtonClicked.value) {
            when (val state = viewModelState.value) {
                is SubState.Success -> {
                    isCheckingCredentials.value = false
                    snackBarHostState.showSnackbar("Credentials are valid!")
                }

                is SubState.Error -> {
                    isCheckingCredentials.value = false
                    snackBarHostState.showSnackbar("Error: ${state.message}")
                }

                is SubState.Loading -> {
                    isCheckingCredentials.value = true
                    // Usually you show a CircularProgressIndicator in the UI
                    // rather than a Snackbar for loading
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Account", maxLines = 1, overflow = TextOverflow.Ellipsis) },
                navigationIcon = {
                    IconButton(onClick = { backNavigation() }) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
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
                    OutlinedTextField(
                        value = viewModel.username,
                        onValueChange = { viewModel.username = it.trim() },
                        label = { Text("Username") },
                        maxLines = 1
                    )
                }
                item {
                    Spacer(Modifier.height(8.dp))
                }
                item {
                    var passwordVisible by remember { mutableStateOf(false) }
                    OutlinedTextField(
                        value = viewModel.password,
                        onValueChange = { viewModel.password = it.trim() },
                        label = { Text("Password") },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            val image = if (passwordVisible)
                                Icons.Filled.Visibility
                            else Icons.Filled.VisibilityOff

                            val description =
                                if (passwordVisible) "Hide password" else "Show password"

                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(imageVector = image, description)
                            }
                        },
                        maxLines = 1
                    )
                }
                item {
                    Spacer(Modifier.height(8.dp))
                }
                item {
                    Button(onClick = {
                        viewModel.fetchSchedule()
                        confirmButtonClicked.value = true
                    }) {
                        Text("Save & Refresh")
                    }
                }
                item {
                    Spacer(Modifier.height(8.dp))
                }
                if (isCheckingCredentials.value) {
                    item {
                        Column(
                            Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Row(
                                Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                ) {
                                CircularProgressIndicator()
                            }
                            Spacer(Modifier.height(8.dp))
                            Row(
                                Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                ) {
                                Text(
                                    "Checking credentials...",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                }
                item {
                    Spacer(Modifier.height(24.dp))
                }
                item {
                    ElevatedCard {
                        SettingComponentSwitch(
                            icon = Icons.Default.School,
                            title = "Teachers view",
                            description = "Otherwise students view is shown",
                            onCheckedChange = {
                                viewModel.teacherView = it
                                viewModel.refetchPlease = true
                            },
                            checked = viewModel.teacherView
                        )
                    }
                }
            }
        }
    }
}