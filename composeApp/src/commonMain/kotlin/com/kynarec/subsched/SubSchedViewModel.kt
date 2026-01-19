package com.kynarec.subsched

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kynarec.shared.SubSchedRepository
import com.kynarec.shared.data.models.SubstitutionSchedule
import com.kynarec.shared.data.parseFullStudentSubstituteSchedule
import com.kynarec.shared.data.parseFullTeacherSubstituteSchedule
import eu.anifantakis.lib.ksafe.compose.mutableStateOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

const val DARK_THEME_KEY = "darkTheme"

class SubSchedViewModel(
    private val repository: SubSchedRepository,
) : ViewModel() {
    private val _state = MutableStateFlow<SubState>(SubState.Loading)
    val state: StateFlow<SubState> = _state

    val kSafe = repository.kSafe
    var isFirstLaunch by kSafe.mutableStateOf(defaultValue = true)

    var username by kSafe.mutableStateOf(defaultValue = "")
    var password by kSafe.mutableStateOf(defaultValue = "")

    var teacherView by kSafe.mutableStateOf(defaultValue = false)
    var refetchPlease by kSafe.mutableStateOf(defaultValue = false)

    // UI preferences:
    suspend fun putBoolean(key: String, value: Boolean) {
        kSafe.put(key, value)
    }
    suspend fun getBoolean(key: String, default: Boolean): Boolean {
        return kSafe.get(key, default)
    }
    var darkThemeFlow = kSafe.getFlow(DARK_THEME_KEY, defaultValue = true)
    var darkThemeDefault by kSafe.mutableStateOf(true)


    fun fetchSchedule() {
        viewModelScope.launch {
            _state.value = SubState.Loading
            if (username.isBlank() || password.isBlank()) {
                _state.value = SubState.Error("Please set credentials in Settings")
                return@launch
            }
            try {
                when (teacherView) {
                    true -> {
                        val data = repository.fetchTeacherInfo(
                            username = username,
                            password = password,
                            news = 1.toString(),
                            days = 5
                        )
                        if (data.isBlank()) {
                            _state.value = SubState.Error("Invalid credentials")
                            return@launch
                        }
                        val parsedData = parseFullTeacherSubstituteSchedule(data)
                        _state.value = SubState.Success(parsedData, System.currentTimeMillis())
                    }
                    false -> {
                        val data = repository.fetchStudentInfo(
                            username = username,
                            password = password,
                            news = 1.toString(),
                            days = 5
                        )
                        if (data.isBlank()) {
                            _state.value = SubState.Error("Invalid credentials")
                            return@launch
                        }
                        val parsedData = parseFullStudentSubstituteSchedule(data)
                        _state.value = SubState.Success(parsedData, System.currentTimeMillis())
                    }
                }
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