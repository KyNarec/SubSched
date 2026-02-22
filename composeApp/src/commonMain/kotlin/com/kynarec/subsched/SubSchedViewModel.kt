package com.kynarec.subsched

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kynarec.shared.SubSchedRepository
import com.kynarec.shared.data.models.SubstitutionSchedule
import com.kynarec.shared.data.parseFullStudentSubstituteSchedule
import com.kynarec.shared.data.parseFullTeacherSubstituteSchedule
import com.kynarec.subsched.ui.navigation.TransitionEffect
import eu.anifantakis.lib.ksafe.compose.mutableStateOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val DARK_THEME_KEY = "darkTheme"
const val TRANSITION_EFFECT_KEY = "transitionEffectKey"
val DEFAULT_TRANSITION_EFFECT = TransitionEffect.SlideHorizontal
const val DEFAULT_REFRESH_INTERVAL = 120
const val REFRESH_INTERVAL_KEY = "refreshInterval"

const val TEXT_STYLE_KEY = "textStyle"

const val CARD_WIDTH_KEY = "cardWidthKey"
const val DEFAULT_CARD_WIDTH = 430

class SubSchedViewModel(
    private val repository: SubSchedRepository,
) : ViewModel() {
    private val _state = MutableStateFlow<SubState>(SubState.Loading)
    val state: StateFlow<SubState> = _state

    private val _lastSuccessfulFetch = MutableStateFlow<SubState>(SubState.Loading)
    val lastSuccessfulFetch: StateFlow<SubState> = _lastSuccessfulFetch

    val kSafe = repository.kSafe
    var isFirstLaunch by kSafe.mutableStateOf(defaultValue = true)

    var username by kSafe.mutableStateOf(defaultValue = "")
    var password by kSafe.mutableStateOf(defaultValue = "")

    var teacherView by kSafe.mutableStateOf(defaultValue = false)

    var autoScroll by kSafe.mutableStateOf(defaultValue = false)
    var refetchPlease by kSafe.mutableStateOf(defaultValue = false)

    var refreshInterval = kSafe.getFlow(REFRESH_INTERVAL_KEY, defaultValue = DEFAULT_REFRESH_INTERVAL)
    var cardWidth = kSafe.getFlow(CARD_WIDTH_KEY, defaultValue = DEFAULT_CARD_WIDTH)


    // UI preferences:
    fun putBoolean(key: String, value: Boolean) {
        kSafe.putDirect(key, value)
    }
    fun putInt(key: String, value: Int) {
        kSafe.putDirect(key, value)
    }
    suspend fun putIntSuspended(key: String, value: Int) {
        kSafe.put(key, value)
    }

    var darkThemeDefault by kSafe.mutableStateOf(true)
    var darkThemeFlow = kSafe.getFlow(DARK_THEME_KEY, defaultValue = darkThemeDefault)

    var transitionEffect by kSafe.mutableStateOf(DEFAULT_TRANSITION_EFFECT, TRANSITION_EFFECT_KEY)
    val transitionEffectFlow = kSafe.getFlow(TRANSITION_EFFECT_KEY, DEFAULT_TRANSITION_EFFECT)
    fun putTransitionEffect(value: TransitionEffect) {
        kSafe.putDirect(TRANSITION_EFFECT_KEY, value)
    }

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
                        val data = withContext(Dispatchers.IO) {
                            repository.fetchTeacherInfo(
                                username = username,
                                password = password,
                                news = 1.toString(),
                                days = 5
                            )
                        }
                        if (data.isBlank()) {
                            _state.value = SubState.Error("Invalid credentials")
                            return@launch
                        }
                        val parsedData = parseFullTeacherSubstituteSchedule(data)
                        if (_state.value is SubState.Success) {
                            _lastSuccessfulFetch.value = _state.value
                        } else {
                            _lastSuccessfulFetch.value = SubState.Success(parsedData, System.currentTimeMillis())
                        }
                        _state.value = SubState.Success(parsedData, System.currentTimeMillis())
                    }
                    false -> {
                        val data = withContext(Dispatchers.IO) {
                            repository.fetchStudentInfo(
                                username = username,
                                password = password,
                                news = 1.toString(),
                                days = 5
                            )
                        }
                        if (data.isBlank()) {
                            _state.value = SubState.Error("Invalid credentials")
                            return@launch
                        }
                        val parsedData = parseFullStudentSubstituteSchedule(data)
                        if (_state.value is SubState.Success) {
                            _lastSuccessfulFetch.value = _state.value
                        } else {
                            _lastSuccessfulFetch.value = SubState.Success(parsedData, System.currentTimeMillis())
                        }
                        _state.value = SubState.Success(parsedData, System.currentTimeMillis())
                    }
                }
            } catch (e: Exception) {
                _state.value = SubState.Error(e.message ?: "Unknown Error")
                println(e.message)
            }
        }
    }


    suspend fun updateRefreshInterval(newSeconds: Int) {
        putIntSuspended(REFRESH_INTERVAL_KEY, newSeconds)
        stopRefreshLoop()
        startRefreshLoop()
    }
    private var refreshLoopJob: Job? = null

    private fun startRefreshLoop() {
        if (refreshLoopJob?.isActive == true)
            return

        refreshLoopJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                val currentInterval = kSafe.getDirect(REFRESH_INTERVAL_KEY, DEFAULT_REFRESH_INTERVAL)

                if (username.isNotBlank() && password.isNotBlank()) {
                    println("Fetching schedule... Next refresh in $currentInterval seconds")
                    fetchSchedule()
                }

                delay(currentInterval * 1000L)
            }
        }
    }

    private fun stopRefreshLoop() {
        refreshLoopJob?.cancel()
    }

    init {
        startRefreshLoop()
    }

    override fun onCleared() {
        super.onCleared()
        stopRefreshLoop()
    }
}


sealed class SubState {
    object Loading : SubState()
    data class Success(val plan: SubstitutionSchedule, val lastFetched: Long) : SubState()
    data class Error(val message: String) : SubState()
}