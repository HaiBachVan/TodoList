package app.todo.ui.task

import android.arch.lifecycle.LiveData
import app.todo.core.data.db.entities.Task
import app.todo.core.data.db.repository.TaskRepository
import javax.inject.Inject

class TaskInteraction @Inject constructor(val repository: TaskRepository) : TaskContract.Interaction {
    override fun loadAllTaskWithId(id: Int): LiveData<List<Task>> = repository.loadAllTaskWithId(id)

    override fun loadAllTask(): LiveData<List<Task>> = repository.loadAllTask()

    override fun insertTask(task: Task) {
        repository.insertTask(task)
    }

    override fun updateTask(task: Task) {
        repository.updateTask(task)
    }
}