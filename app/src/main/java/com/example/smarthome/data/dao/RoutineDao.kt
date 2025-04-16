package com.example.smarthome.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.smarthome.data.model.RoutineTask
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineDao {
    @Query("SELECT * FROM routines")
    fun getAllRoutines(): Flow<List<RoutineTask>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutine(routine: RoutineTask)

    @Update
    suspend fun updateRoutine(routine: RoutineTask)

    @Delete
    suspend fun deleteRoutine(routine: RoutineTask)

    @Query("SELECT COUNT(*) FROM routines")
    suspend fun getRoutineCount(): Int

    @Query("SELECT * FROM routines WHERE id = :id")
    suspend fun getRoutineById(id: String): RoutineTask?
}

