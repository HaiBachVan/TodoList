package app.todo.core.data.db.util

import android.arch.persistence.room.TypeConverter
import java.util.*

object Converters {

    @TypeConverter
    fun fromTimestamp(value: Long): Date = Date(value)

    @TypeConverter
    fun dateToTimestamp(date: Date): Long = date.time

}