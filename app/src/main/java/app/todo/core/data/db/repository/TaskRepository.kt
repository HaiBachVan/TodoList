package app.todo.core.data.db.repository

import android.arch.lifecycle.LiveData
import app.todo.core.data.db.dao.TaskDao
import app.todo.core.data.db.entities.Task
import javax.inject.Inject

class TaskRepository @Inject constructor(val taskDao: TaskDao) {
    fun loadAllTask(): LiveData<List<Task>> = taskDao.loadAllTask()

    fun loadAllTaskWithId(id: Int): LiveData<List<Task>> = taskDao.loadAllTaskWithId(id)

    fun insertTask(task: Task) {
        taskDao.insertTask(task)
    }

    fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }
}