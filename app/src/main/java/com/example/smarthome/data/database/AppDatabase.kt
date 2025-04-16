package com.example.smarthome.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.smarthome.data.dao.RoutineDao
import com.example.smarthome.data.model.RecurrenceType
import com.example.smarthome.data.model.RecurrenceTypeConverter
import com.example.smarthome.data.model.RoutineTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [RoutineTask::class], version = 1, exportSchema = false)
@TypeConverters(RecurrenceTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun routineDao(): RoutineDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "smart_home_database"
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Pre-populate with sample data when database is created
                            INSTANCE?.let { database ->
                                scope.launch(Dispatchers.IO) {
                                    populateDatabase(database.routineDao())
                                }
                            }
                        }
                    })
                    .build()

                INSTANCE = instance
                instance
            }
        }

        // Pre-populate with sample data
        private suspend fun populateDatabase(routineDao: RoutineDao) {
            // Only add sample data if the database is empty
            if (routineDao.getRoutineCount() == 0) {
                val sampleRoutines = listOf(
                    RoutineTask(
                        id = "1",
                        name = "Security Lights on",
                        hour = 6,
                        minute = 0,
                        recurrence = RecurrenceType.EVERY_DAY
                    ),
                    RoutineTask(
                        id = "2",
                        name = "Security Lights on",
                        hour = 18,
                        minute = 0,
                        recurrence = RecurrenceType.EVERY_DAY
                    ),
                    RoutineTask(
                        id = "3",
                        name = "Coffee Maker on",
                        hour = 7,
                        minute = 0,
                        recurrence = RecurrenceType.WEEK_DAYS
                    )
                )

                sampleRoutines.forEach { routine ->
                    routineDao.insertRoutine(routine)
                }
            }
        }
    }
}

