package com.example.smarthome.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.smarthome.data.model.RoutineTask
import com.example.smarthome.data.repository.RoutineRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class RoutineViewModel(
    application: Application, // Add the Application parameter back
    private val repository: RoutineRepository
) : AndroidViewModel(application) { // Pass 'application' to the super constructor

    // Collect all routines as StateFlow
    val allRoutines: StateFlow<List<RoutineTask>> = repository.allRoutines
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    // Insert a new routine
    fun insertRoutine(name: String, hour: Int, minute: Int, recurrence: com.example.smarthome.data.model.RecurrenceType) {
        viewModelScope.launch {
            val newRoutine = RoutineTask(
                id = UUID.randomUUID().toString(),
                name = name,
                hour = hour,
                minute = minute,
                recurrence = recurrence
            )
            repository.insertRoutine(newRoutine)
        }
    }

    // Update an existing routine
    fun updateRoutine(routine: RoutineTask) {
        viewModelScope.launch {
            repository.updateRoutine(routine)
        }
    }

    // Delete a routine
    fun deleteRoutine(routine: RoutineTask) {
        viewModelScope.launch {
            repository.deleteRoutine(routine)
        }
    }
}