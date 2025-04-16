package com.example.smarthome.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter


@Entity(tableName = "routines")
data class RoutineTask(
    @PrimaryKey val id: String,
    val name: String,
    val hour: Int,
    val minute: Int,
    val recurrence: RecurrenceType
)

enum class RecurrenceType {
    EVERY_DAY,
    WEEK_DAYS,
    WEEKEND,
    WEEKLY,
    MONTHLY,
    YEARLY;

    companion object {
        fun fromString(value: String): RecurrenceType {
            return when (value.lowercase()) {
                "every day" -> EVERY_DAY
                "week days" -> WEEK_DAYS
                "weekend" -> WEEKEND
                "weekly" -> WEEKLY
                "monthly" -> MONTHLY
                "yearly" -> YEARLY
                else -> EVERY_DAY
            }
        }
    }
}

class RecurrenceTypeConverter {
    @TypeConverter
    fun fromRecurrenceType(recurrenceType: RecurrenceType): String {
        return recurrenceType.name
    }

    @TypeConverter
    fun toRecurrenceType(value: String): RecurrenceType {
        return try {
            RecurrenceType.valueOf(value)
        } catch (e: IllegalArgumentException) {
            RecurrenceType.EVERY_DAY
        }
    }
}

fun RecurrenceType.toDisplayString(): String {
    return when(this) {
        RecurrenceType.EVERY_DAY -> "Every day"
        RecurrenceType.WEEK_DAYS -> "Week days"
        RecurrenceType.WEEKEND -> "Weekend"
        RecurrenceType.WEEKLY -> "Weekly"
        RecurrenceType.MONTHLY -> "Monthly"
        RecurrenceType.YEARLY -> "Yearly"
    }
}
