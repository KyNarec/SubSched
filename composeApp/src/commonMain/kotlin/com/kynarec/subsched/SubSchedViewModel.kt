package com.kynarec.subsched

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kynarec.shared.SubSchedRepository
import com.kynarec.shared.data.models.SubstitutionSchedule
import com.kynarec.shared.data.parseFullSubstituteSchedule
import eu.anifantakis.lib.ksafe.compose.mutableStateOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SubSchedViewModel(
    private val repository: SubSchedRepository,
) : ViewModel() {
    private val _state = MutableStateFlow<SubState>(SubState.Loading)
    val state: StateFlow<SubState> = _state

    val kSafe = repository.kSafe
    var username by kSafe.mutableStateOf(defaultValue = "")
    var password by kSafe.mutableStateOf(defaultValue = "")


    fun fetchSchedule() {
        viewModelScope.launch {
            _state.value = SubState.Loading
            if (username.isBlank() || password.isBlank()) {
                _state.value = SubState.Error("Please set credentials in Settings")
                return@launch
            }
            try {
                val data = repository.fetchTeacherInfo(
                    username = username,
                    password = password,
                    type = "teacher",
                    news = 1.toString(),
                    days = 4
                )
                if (data.isBlank()) {
                    _state.value = SubState.Error("Invalid credentials")
                    return@launch
                }
                val parsedData = parseFullSubstituteSchedule(data)
                _state.value = SubState.Success(parsedData, System.currentTimeMillis())
            } catch (e: Exception) {
                _state.value = SubState.Error(e.message ?: "Unknown Error")
            }
        }
    }

    fun updateCredentials(user: String, pass: String) {
        username = user
        password = pass
    }
}

sealed class SubState {
    object Loading : SubState()
    data class Success(val plan: SubstitutionSchedule, val lastFetched: Long) : SubState()
    data class Error(val message: String) : SubState()
}