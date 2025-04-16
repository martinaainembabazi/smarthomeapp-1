package com.example.smarthome

import android.app.Application
import com.example.smarthome.data.database.AppDatabase
import com.example.smarthome.data.repository.RoutineRepository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob



class SmartHomeApplication : Application() {

    // Application scope
    private val applicationScope = CoroutineScope(SupervisorJob())

    // Database instance
    private val database by lazy { AppDatabase.getDatabase(this, applicationScope) }

    // Repository instance
    val routineRepository: RoutineRepository by lazy { RoutineRepository(database.routineDao(), this) }
}

