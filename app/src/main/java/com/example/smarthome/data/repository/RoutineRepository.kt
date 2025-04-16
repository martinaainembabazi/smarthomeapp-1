package com.example.smarthome.data.repository

import android.app.Application
import com.example.smarthome.data.dao.RoutineDao
import com.example.smarthome.data.model.RoutineTask
import kotlinx.coroutines.flow.Flow

class RoutineRepository(
    private val routineDao: RoutineDao,
    private val application: Application // Keep the Application parameter
) {



    // Get all routines as a Flow
    val allRoutines: Flow<List<RoutineTask>> = routineDao.getAllRoutines()

    // Add a new routine
    suspend fun insertRoutine(routine: RoutineTask) {
        routineDao.insertRoutine(routine)
    }

    // Update an existing routine
    suspend fun updateRoutine(routine: RoutineTask) {
        routineDao.updateRoutine(routine)
    }

    // Delete a routine
    suspend fun deleteRoutine(routine: RoutineTask) {
        routineDao.deleteRoutine(routine)
    }

    // Get a specific routine by ID
    suspend fun getRoutineById(id: String): RoutineTask? {
        return routineDao.getRoutineById(id)
    }
}