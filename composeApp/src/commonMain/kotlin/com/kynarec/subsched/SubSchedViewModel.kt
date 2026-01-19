package com.kynarec.subsched

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kynarec.shared.SubSchedRepository
import com.kynarec.shared.data.models.SubstitutionSchedule
import com.kynarec.shared.data.parseFullSubstituteSchedule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SubSchedViewModel(private val repository: SubSchedRepository) : ViewModel() {
    private val _state = MutableStateFlow<SubState>(SubState.Loading)
    val state: StateFlow<SubState> = _state

    fun fetchSchedule() {
        viewModelScope.launch {
            _state.value = SubState.Loading
            try {
                val data = repository.fetchTeacherInfo(

                )
                val parsedData = parseFullSubstituteSchedule(data)
                _state.value = SubState.Success(parsedData)
            } catch (e: Exception) {
                _state.value = SubState.Error(e.message ?: "Unknown Error")
            }
        }
    }
}

sealed class SubState {
    object Loading : SubState()
    data class Success(val plan: SubstitutionSchedule) : SubState()
    data class Error(val message: String) : SubState()
}