package app.todo.ui.task

import android.arch.lifecycle.LiveData
import app.todo.core.data.db.entities.Task
import app.todo.core.presentation.BaseContract

interface TaskContract {
    interface View : BaseContract.View

    interface Interaction : BaseContract.Interaction {
        fun loadAllTask(): LiveData<List<Task>>
        fun loadAllTaskWithId(id: Int): LiveData<List<Task>>
        fun insertTask(task: Task)
        fun updateTask(task: Task)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun onClickAddToDo(task: Task)
        fun onViewInitialized(id: Int): LiveData<List<Task>>
    }

}