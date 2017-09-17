package app.todo.core.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import app.todo.core.data.db.dao.ListNameDao
import app.todo.core.data.db.dao.TaskDao
import app.todo.core.data.db.entities.ListName
import app.todo.core.data.db.entities.Task
import app.todo.core.data.db.util.Converters

@Database(entities = arrayOf(ListName::class, Task::class), version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class DatabaseManager : RoomDatabase() {

    abstract fun listNameDao(): ListNameDao
    abstract fun taskDao(): TaskDao

    companion object {
        const val DATABASE_NAME = "todo-list.db"
    }

}