package com.example.smarthome.ui.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.smarthome.data.repository.RoutineRepository

class RoutineViewModelFactory(
    private val application: Application, // Add the Application parameter
    private val repository: RoutineRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoutineViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RoutineViewModel(application, repository) as T // Pass 'application' to RoutineViewModel
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}