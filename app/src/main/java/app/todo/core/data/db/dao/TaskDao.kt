package app.todo.core.data.db.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import app.todo.core.data.db.entities.Task

@Dao
interface TaskDao {
    @Query("SELECT * FROM task")
    fun loadAllTask(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE listNameId = :arg0")
    fun loadAllTaskWithId(id: Int): LiveData<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task: Task)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateTask(task: Task)

    @Delete
    fun deleteTask(task: Task)
}